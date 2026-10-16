pipeline {
    agent any

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.11'
        MAVEN = 'C:\\Rayssoftware\\DevOp\\apache-maven-3.10.0\\bin\\mvn.cmd'
        PATH = "${JAVA_HOME}\\bin;${env.PATH}"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/HemaSingh18/Test_Project04.git',
                    branch: 'main'
            }
        }

        stage('Build New WAR') {
            steps {
                bat 'call "%MAVEN%" clean package'
            }
        }

        stage('Build Web and Pull MySQL') {
            steps {
                bat 'docker-compose build webapp'
                bat 'docker-compose pull db'
            }
        }

        stage('Docker Tagging') {
            steps {
                bat 'docker tag web:latest hemasingh18/webapp:latest'
                bat 'docker tag mysql:8.0 hemasingh18/mysql:8.0'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creads',
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_PASS'
                    )
                ]) {
                    bat 'docker login -u %DH_USER% -p %DH_PASS%'
                }
            }
        }

        stage('Docker Push') {
            steps {
                bat 'docker push hemasingh18/webapp:latest'
                bat 'docker push hemasingh18/mysql:8.0'
            }
        }
    }
}