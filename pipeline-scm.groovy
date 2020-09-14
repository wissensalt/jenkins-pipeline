pipeline {
    agent any

    environment {
        DEMO='PIPELINE-SCM-GIT'
    }

    stages {
        stage('stage-1') {
            steps {
                echo "This is build number $BUILD_NUMBER of DEMO $DEMO"
                sh '''
                    echo "Using multi-line shell step"
                    chmod +x test.sh
                    ./test.sh
                '''
            }
        }
    }
}