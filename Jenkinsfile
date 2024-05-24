pipeline {
    agent any

    // environment {
    //     EMAIL_TO = "tsaadimas@hua.gr"
    // }

    // Before running the jenkins pipeline make sure that:
    // 1) From jenkins vm with user jenkins you can
    // connect to the db-vm-01, backend-vm-01, frontend-vm-01

    // 2) Make sure to change the variable db_server_ip in
    // the stage 'Deploy spring boot app' to the ip of the 
    // vm that hosts the db

    // 3) Make sure to change the variable backend_server_ip in
    // the stage 'Deploy frontend' to the ip of the 
    // vm that hosts the backend

    // 4) Make sure to change the variable frontend_server_ip in
    // the stage 'Deploy frontend' to the ip of the 
    // vm that hosts the frontend
    

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
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible'
            }
        }
        stage('install ansible prerequisites') {
            steps {
                sh '''
                    ansible-galaxy install geerlingguy.postgresql
                '''
            }
        }
        stage('Install postgres') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l db-vm-01 ~/workspace/ansible/playbooks/postgres.yaml
                '''
            }
        }

        stage('Deploy spring boot app') {
            steps {
                sh '''
                   # replace dbserver in host_vars
                    # sed -i 's/dbserver/4.211.249.239/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                   # replace workingdir in host_vars
                    # sed -i 's/vagrant/azureuser/g' ~/workspace/ansible/host_vars/appserver-vm.yaml
                '''
                sh '''
                    # edit host var for appserver

                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l backend-vm-01 -e db_server_ip=35.240.9.21 -e user=jenkins -e group=jenkins ~/workspace/ansible/playbooks/spring.yaml
                '''
            }
        }
       stage('Deploy frontend') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l frontend-vm-01 -e backend_server_ip=34.22.253.155 -e frontend_server_ip=34.76.184.187 ~/workspace/ansible/playbooks/vuejs.yaml
                '''
            }
       }
    }

    // post {
    //     always {
    //         mail  to: "${EMAIL_TO}", body: "Project ${env.JOB_NAME} <br>, Build status ${currentBuild.currentResult} <br> Build Number: ${env.BUILD_NUMBER} <br> Build URL: ${env.BUILD_URL}", subject: "JENKINS: Project name -> ${env.JOB_NAME}, Build -> ${currentBuild.currentResult}"
    //     }
    // }
}
