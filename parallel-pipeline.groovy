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

            parallel {
                stage('IOS') {
                    steps {
                        echo "Building release ${RELEASE} version ${STAGE_NAME} with log level ${LOG_LEVEL}"
                    }
                }
                stage('Android') {
                    steps {
                        echo "Building release ${RELEASE} version ${STAGE_NAME} with log level ${LOG_LEVEL}"
                    }
                }
                stage('Ubuntu') {
                    steps {
                        echo "Building release ${RELEASE} version ${STAGE_NAME} with log level ${LOG_LEVEL}"
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo "Testing release ${RELEASE}"
            }
        }

        stage('Deploy') {
            input {
                message 'Are u sure to deploy ?'
                ok 'Do it !'
                parameters {
                    string(name: 'TARGET_ENVIRONMENT', defaultValue: 'PROD', description: 'Target deployment environment')
                }
            }
            steps {
                echo "Deploying release ${RELEASE} to environment ${TARGET_ENVIRONMENT}"
            }
        }
    }

    post {
        always {
            echo 'Print message wether deployment is successfull or failed'
        }
    }
}