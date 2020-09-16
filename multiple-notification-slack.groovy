def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
]

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
                sh 'chmod +x api-key-info.sh'
                withCredentials([string(credentialsId: '93c06d13-5e8c-4be2-8a10-271c5217e8f0', variable: 'API_KEY')]) {
                    sh '''
                        ./api-key-info.sh
                    '''
                }
            }
        }

        stage('Test') {
            steps {
                echo "Testing release ${RELEASE}"
                writeFile file: 'test-result.txt', text: 'passed'
            }
        }

        stage ('Deploy') {       
            environment {                
                CHECK_DEPLOY = 'N'
            }         

            steps {
                script {
                    try {
                        CHECK_DEPLOY = input message: 'Sure to deploy ?'
                        echo "CHECK_DEPLOY value ${CHECK_DEPLOY}"                    
                    } catch(e) {
                        echo "Exception caught : ${e}"
                        throw new Exception()
                    }
                }                                
            }
        }
    }

    post {    
        success {        
            slackSend channel: '#dev-deployment', color: COLOR_MAP[currentBuild.currentResult], 
                                message: "RELEASE ${RELEASE}, success: ${currentBuild.fullDisplayName}."
        }

        failure {        
            slackSend channel: '#dev-deployment', color: COLOR_MAP[currentBuild.currentResult], 
                                message: "RELEASE ${RELEASE}, failed: ${currentBuild.fullDisplayName}."
        }

        always {
            archiveArtifacts 'test-result.txt'
        }
    }
}