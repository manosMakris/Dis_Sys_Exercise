pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }

    environment {
        DOCKER_TOKEN = credentials('docker-push-secret')
        DOCKER_USER = 'it21773'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_EMAIL = 'it21773@hua.gr'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'git@github.com:manosMakris/Dis_Sys_Exercise.git'
            }
        }
        stage('Test') {
            steps {
                sh 'chmod +x ./mvnw && ./mvnw test'
            }
        }
        
        stage('run pushBackend pipeline') {
            steps {
                build job: 'push-backend-image'
            }
        }

        stage('run pushFrontend pipeline') {
            steps {
                build job: 'push-frontend-image'
            }
        }

//         stage('Set up .dockerconfig.json file') {
//             steps {
//                 sh '''
//                     auth=$(echo -n "$DOCKER_USER:$DOCKER_TOKEN" | base64)
//                     echo '{
//                             "auths": {
//                                 "https://$DOCKER_SERVER":{
//                                     "username":"$DOCKER_USER",
//                                     "password":"$DOCKER_TOKEN",
//                                     "email":"$DOCKER_EMAIL",
//                                     "auth":"$auth"
//                                 }
//                             }
//                         }' > k8s/.dockerconfig.json
//                 '''
//             }
//         }

//         stage('Create a secret based on .dockerconfigjson file') {
//             steps {
//                 sh '''
//                     kubectl create secret docker-registry registry-credentials --from-file=.dockerconfigjson=k8s/.dockerconfig.json
//                 '''
//             }
//         }

        stage('Install database with kubernetes') {
            steps {
                sh '''
                    kubectl apply -f k8s/postgres/postgres-pvc.yaml
                    kubectl apply -f k8s/postgres/postgres-deployment.yaml
                    kubectl apply -f k8s/postgres/postgres-svc.yaml
                '''
            }
        }

        stage('Install backend with kubernetes') {
            steps {
                sh '''
                    kubectl apply -f k8s/spring/spring-deployment.yaml
                    kubectl apply -f k8s/spring/spring-svc.yaml
                '''
            }
        }

        stage('Install frontend with kubernetes') {
            steps {
                sh '''
                    kubectl apply -f k8s/vuejs/vue-deployment.yaml
                    kubectl apply -f k8s/vuejs/vue-svc.yaml
                    kubectl apply -f k8s/vuejs/vue-ingress.yaml
                '''
            }
        }

    }

}