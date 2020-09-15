pipeline {
    agent any 

    environment {
        RELEASE = '1.0.0.RELEASE'
    }

    stages {
        stage('Build') {
            environment {
                LOG_LEVEL = 'INFO'
            }
            steps {
                echo "Building release ${RELEASE} with log level ${LOG_LEVEL}"
            }
        }
        stage('Test') {
            steps {
                echo "Testing release ${RELEASE}"
                writeFile file: 'test-result.txt', text: 'passed'
            }
        }
    }

    post{
        success {
            archiveArtifacts 'test-result.txt'
        }
    }
}