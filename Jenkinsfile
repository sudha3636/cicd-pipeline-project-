pipeline {
    agent any

    environment {
        DOCKERHUB_USER = 'sudha3636'
        APP_NAME       = 'cicd-app'
        EC2_IP         = '43.205.214.33'
        GIT_REPO       = 'https://github.com/sudha3636/cicd-pipeline-project-.git'
        BRANCH         = 'main'
    }

    stages {

        stage('Clone (Force Main Branch)') {
            steps {
                deleteDir()  // clean workspace (prevents branch confusion)

                git branch: "${BRANCH}",
                    credentialsId: 'github-creds',
                    url: "${GIT_REPO}"
            }
        }

        stage('Debug Branch') {
            steps {
                sh '''
                    echo "Current Branch:"
                    git branch
                    echo "Last Commit:"
                    git log -1 --oneline
                '''
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
                sh '''
                    docker build \
                        -t ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER} \
                        -t ${DOCKERHUB_USER}/${APP_NAME}:latest .
                '''
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
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER}
                        docker push ${DOCKERHUB_USER}/${APP_NAME}:latest
                        docker logout
                    '''
                }
            }
        }

        stage('Update Image Tag (GitOps)') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-creds',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_PASS'
                )]) {
                    sh """
                        git config user.email 'jenkins@cicd.com'
                        git config user.name 'Jenkins'

                        echo "Updating deployment.yaml with new image..."
                        sed -i "s|image: .*|image: ${DOCKERHUB_USER}/${APP_NAME}:${BUILD_NUMBER}|" k8s/deployment.yaml

                        git add k8s/deployment.yaml

                        git commit -m "[Jenkins] Update image to ${BUILD_NUMBER}" || echo "No changes to commit"

                        git push https://${GIT_USER}:${GIT_PASS}@github.com/sudha3636/cicd-pipeline-project-.git ${BRANCH}
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ SUCCESS: Build #${BUILD_NUMBER}"
            echo "🚀 ArgoCD will auto-sync to EC2 cluster"
            echo "🌐 App URL (NodePort): http://${EC2_IP}:30080"
        }

        failure {
            echo "❌ Pipeline FAILED — check logs carefully"
        }

        always {
            sh 'docker image prune -f'
        }
    }
}
