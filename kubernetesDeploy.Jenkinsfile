pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
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
                build job: 'pushBackend'
            }
        }

        stage('run pushFrontend pipeline') {
            steps {
                build job: 'pushFrontend'
            }
        }

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
                    kubectl apply -f k8s/spring/spring-ingress.yaml
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