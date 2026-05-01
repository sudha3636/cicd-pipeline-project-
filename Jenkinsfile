pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    environment {
        DOCKERHUB_USER = 'sudha3636'
        APP_NAME       = 'cicd-app'
        EC2_IP         = '43.205.214.33'
    }

    stages {

        stage('Clone') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-creds',
                    url: 'https://github.com/sudha3636/cicd-pipeline-project-.git'
            }
        }

        stage('Maven Build & Test') {
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
                    docker build \
                        -t ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER} \
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
                        echo $DOCKER_PASS | \
                            docker login -u $DOCKER_USER --password-stdin
                        docker push ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER}
                        docker push ${DOCKERHUB_USER}/${APP_NAME}:latest
                        docker logout
                    '''
                }
            }
        }

        stage('Update Image Tag — GitOps') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-creds',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_PASS'
                )]) {
                    sh """
                        git config user.email 'jenkins@cicd.com'
                        git config user.name 'Jenkins'
                        sed -i 's|image: .*|image: ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER}|g' \
                            k8s/deployment.yaml
                        git add k8s/deployment.yaml
                        git commit -m '[Jenkins] Update image tag to #${BUILD_NUMBER}'
                        git push https://${GIT_USER}:${GIT_PASS}@github.com/sudha3636/cicd-pipeline-project-.git main
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Done! ArgoCD will deploy build #${BUILD_NUMBER} to EC2"
            echo "App URL: http://${EC2_IP}:30080"
        }
        failure {
            echo '❌ Pipeline failed — check logs'
        }
        always {
            sh 'docker image prune -f'
        }
    }
}
