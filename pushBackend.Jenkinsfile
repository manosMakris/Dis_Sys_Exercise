pipeline {
    agent any

    // Before running the jenkins pipeline make sure that:
    // 1) From jenkins vm with user jenkins you can
    // connect to the docker-server-app-02

    // 2) Change the VITE_BACKEND variable in frontend
    // repo to the ip of docker-server-app-02

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }

    environment {
        // EMAIL_TO = "tsaadimas@hua.gr"
        DOCKER_TOKEN = credentials('docker-push-secret')
        DOCKER_USER = 'it21773'
        DOCKER_SERVER = 'ghcr.io'
//         DOCKER_PREFIX = '$DOCKER_SERVER/$DOCKER_USER/ds-spring'
// ghcr.io/it21773/ds-spring
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
        stage('Docker build and push') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    sudo usermod -aG docker jenkins && docker build --rm -t $DOCKER_SERVER/$DOCKER_USER/ds-spring:$TAG -t $DOCKER_SERVER/$DOCKER_USER/ds-spring:latest  -f nonroot.Dockerfile .
                    echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                    sudo usermod -aG docker jenkins && docker push $DOCKER_SERVER/$DOCKER_USER/ds-spring --all-tags
                '''
            }
        }

        stage('set latest backend image') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    kubectl set image deployment/spring-deployment spring=$DOCKER_SERVER/$DOCKER_USER/ds-spring:$TAG
                    kubectl rollout status deployment spring-deployment --watch --timeout=2m
                '''
            }
        }
    }
}