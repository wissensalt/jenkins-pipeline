pipeline {
    agent {
        docker {
            image 'mongo:latest'
        }
    }

    stages {
        stage('Verify') {
            steps {
                echo 'docker version'
                sh '''
                    docker image ls
                    docker container ls --all
                '''

                sh 'printenv'
                sh 'ls -l "$WORKSPACE"'
            }
        }

        stage('Build') {
            steps {
                sh 'dotnet build '
            }
        }
    }
}