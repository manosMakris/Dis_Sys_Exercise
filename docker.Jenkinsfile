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
        DOCKER_USER = 'manosmakris'
        DOCKER_SERVER = 'ghcr.io'
        DOCKER_PREFIX = '$DOCKER_SERVER/$DOCKER_USER/ds-spring'
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
        // stage('Docker build and push') {
        //     steps {
        //         sh '''
        //             HEAD_COMMIT=$(git rev-parse --short HEAD)
        //             TAG=$HEAD_COMMIT-$BUILD_ID
        //             docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest  -f nonroot.Dockerfile .
        //             echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
        //             docker push $DOCKER_PREFIX --all-tags
        //         '''
        //     }
        // }
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible'
            }
        }
        stage('Install docker') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l docker-server-app-02 -e user=jenkins ~/workspace/ansible/playbooks/docker.yaml
                '''
            }
        }
        stage('Install docker compose') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l docker-server-app-02 ~/workspace/ansible/playbooks/docker-comp-install.yaml
                '''
            }
        }
        stage('Install project with docker compose') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l docker-server-app-02 -e user=jenkins -e group=jenkins ~/workspace/ansible/playbooks/dockerize-app.yaml
                '''
            }
        }
    }

    // post {
    //     always {
    //         mail  to: "tsadimas@hua.gr", body: "Project ${env.JOB_NAME} <br>, Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER} <br> Build URL: ${env.BUILD_URL}", subject: "JENKINS: Project name -> ${env.JOB_NAME}, Build -> ${currentBuild.currentResult}"
    //     }
    // }
}