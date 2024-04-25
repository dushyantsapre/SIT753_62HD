pipeline {
    agent { label 'Jenkins-Agent' }
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
     environment {
	    APP_NAME = "sit653_62hd"
            RELEASE = "1.0.0"
            DOCKER_USER = "dushyantsapre1981"
            DOCKER_PASS = 'dockerhub'
            IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
            IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
	    JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")
    }
    stages{
	stage("Cleanup Workspace"){
	 	steps {
                    cleanWs()
                }
	}
	
        stage("Checkout from SCM"){
                steps {
                    git branch: 'main', credentialsId: 'github', url: 'https://github.com/dushyantsapre/SIT753_62HD'
                }
        }

        stage("Build"){
            steps {
                echo 'Building the project...'
                sh "mvn clean package"
            }

       }

       stage("Test"){
           steps {
                 echo 'Running tests...'
                 sh "mvn test"
           }
       }

       stage("Code Quality Analysis"){
	   steps {
	       script {
		    withSonarQubeEnv(credentialsId: 'jenkins-sonarqube-token') { 
		    sh "mvn sonar:sonar"
		    }
	       script {
		    waitForQualityGate abortPipeline: false, credentialsId: 'jenkins-sonarqube-token'
		    }
		}	
	    }
	}

	stage("Deploy") {
            steps {
                script {
                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image = docker.build "${IMAGE_NAME}"
                    }
                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image.push("${IMAGE_TAG}")
                        docker_image.push('latest')
                    }
                }
            }
        }
	stage("Trivy Scan & Cleanup Artifacts") {
           steps {
               script {
	            sh ('docker run -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image dushyantsapre1981/sit653_62hd:latest --no-progress --scanners vuln  --exit-code 0 --severity HIGH,CRITICAL --format table')
               }
	       script {
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker rmi ${IMAGE_NAME}:latest"
               }
           }
        }
	stage("Release") {
            steps {
                script {
                    sh "curl -v -k --user admin:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'ec2-3-107-52-37.ap-southeast-2.compute.amazonaws.com:8080/job/SIT753_62HD_CD/buildWithParameters?token=gitops-token'"
                }
            }
       }
    }
	post {
           failure {
                 emailext body: '''${SCRIPT, template="groovy-html.template"}''', 
                          subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - Failed", 
                          mimeType: 'text/html',to: "dushyant.sapre1981@gmail.com"
          }
          success {
                emailext body: '''${SCRIPT, template="groovy-html.template"}''', 
                         subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - Successful", 
                         mimeType: 'text/html',to: "dushyant.sapre1981@gmail.com"
          }      
        }
}
