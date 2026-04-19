pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    environment {
        DOCKERHUB_USER = 'sudha3636
        APP_NAME       = 'cicd-app'
        EC2_IP         = '13.127.94.193'
    }

    stages {

        stage('Clone') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/sudha3636/cicd-pipeline-project-.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean package'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh """
                    docker build -t ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER} \
                                 -t ${DOCKERHUB_USER}/${APP_NAME}:latest .
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
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${DOCKERHUB_USER}/${APP_NAME}:latest
                        docker push ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER}
                        docker logout
                    '''
                }
            }
        }

        stage('Update Image Tag in k8s') {
            steps {
                sh """
                    sed -i 's|image: .*|image: ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER}|g' \
                        k8s/deployment.yaml
                    git config user.email 'jenkins@cicd.com'
                    git config user.name 'Jenkins'
                    git add k8s/deployment.yaml
                    git commit -m 'Update image tag to build #${BUILD_NUMBER}'
                    git push origin main
                """
            }
        }
    }

    post {
        success {
            echo " Done! ArgoCD will auto-deploy build #${BUILD_NUMBER} to EC2"
        }
        failure {
            echo ' Pipeline failed — check logs above'
        }
        always {
            sh 'docker image prune -f'
        }
    }
}
