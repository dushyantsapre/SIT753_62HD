pipeline {
    agent { label 'Jenkins-Agent' }
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
   
    stages{
        stage("Checkout from SCM"){
                steps {
                    git branch: 'main', credentialsId: 'github', url: 'https://github.com/dushyantsapre/SIT753_62HD'
                }
        }

        stage("Build"){
            steps {
                sh "mvn clean package"
            }

       }

       stage("Test"){
           steps {
                 sh "mvn test"
           }
       }
    }
}
