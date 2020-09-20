pipeline {
    agent {
        docker {
            image 'mcr.microsoft.com/dotnet/code/sdk:3.1.101'
        }
    }

    stages {
        stage('Verify') {
            steps {
                echo 'docker version'
                sh '''
                    dotnet --list-sdks
                    dotnet --list-runtimes
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