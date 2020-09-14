pipeline {
    agent any

    environment {
        RELEASE = '1.0.0.RELEASE'
    } 

    stages {
        stage ('Build') {
            agent any
            environment {
                LOG_LEVEL = 'INFO'
            }
            steps {
                echo "Building release ${RELEASE} with log level ${LOG_LEVEL}"
            }
        }
        stage ('Deploy') {
            input message 'Sure to deploy ?'
            ok 'Do it !'
            parameters {
                string(name: 'TARGET_ENVIRONMENT', defaultValue: 'PROD', description: 'Target deployment environment')
            }
            steps {
                echo "Deploying relase ${RELEASE} to environment ${TARGET_ENVIRONMENT}"
            }
        }
    }

    post {
        always {
            echo 'Print message wether the deployment is successfull or failed'
        }
    }
}