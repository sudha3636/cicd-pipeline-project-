pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Apache Maven 3.9.14'
    }

    environment {
        DOCKERHUB_USER = 'sudha3636'
        APP_NAME       = 'cicd-app'
        EC2_IP         = '13.126.218.214'
    }

    stages {

        stage('Clone') {
            steps {
                deleteDir()
                git branch: 'main',
                    credentialsId: 'github-creds',
                    url: 'https://github.com/sudha3636/cicd-pipeline-project-.git'
            }
        }

        stage('Debug') {
        steps {
            bat 'echo ===== PATH ====='
            bat 'echo %PATH%'
            bat 'echo ===== JAVA ====='
            bat 'java -version'
            bat 'echo ===== MAVEN ====='
            bat 'mvn -version'
        }
    }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Verify Tools') {
    steps {
        bat 'where docker'
        bat 'where git'
        bat 'where powershell'
    }
}

        stage('Docker Build') {
            steps {
                bat """
                docker build -t %DOCKERHUB_USER%/%APP_NAME%:%BUILD_NUMBER% -t %DOCKERHUB_USER%/%APP_NAME%:latest .
                """
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat """
                    echo %DOCKER_PASS% | docker login docker.io -u %DOCKER_USER% --password-stdin
                    docker push %DOCKERHUB_USER%/%APP_NAME%:%BUILD_NUMBER%
                    docker push %DOCKERHUB_USER%/%APP_NAME%:latest
                    docker logout
                    """
                }
            }
        }

        stage('Update GitOps') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-creds',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_PASS'
                )]) {
                    bat """
                    git config user.email "jenkins@cicd.com"
                    git config user.name "Jenkins"

                    powershell -Command "(Get-Content k8s/deployment.yaml) -replace 'image: .*', 'image: %DOCKERHUB_USER%/%APP_NAME%:%BUILD_NUMBER%' | Set-Content k8s/deployment.yaml"

                    git add k8s/deployment.yaml
                    git commit -m "[Jenkins] Update image tag to #%BUILD_NUMBER%"
                    git push https://%GIT_USER%:%GIT_PASS%@github.com/sudha3636/cicd-pipeline-project-.git main
                    """
                }
            }
        }
    }

    post {
        success {
            echo " ArgoCD will sync automatically"
            echo "App URL: http://${EC2_IP}:30080"
        }
        failure {
            echo " Pipeline failed"
        }
    }
}
