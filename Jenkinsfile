pipeline {
    agent { label 'Jenkins-Agent' }
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
     environment {
	    APP_NAME = "SIT653_62HD"
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
    }
}
