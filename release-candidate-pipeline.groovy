pipeline {
    agent any 

    parameters {
        booleanParam(name: 'RC', defaultValue: false, description: 'Is this release candidate ?')
    }

    environment {
        VERSION = '1.0.0'
        VERSION_RC = 'RC.2'    
    }

    stages {
        stage('Audit Tools') {
            steps {
                sh '''
                    git version
                    docker version
                '''
            }

            stage('Build') {
                environment {
                    VERSION_SUFFIX = "${sh(script: 'if [ "${RC}" == "false" ] ; then echo -n "${VERSION_RC}+ci.${BUILD_NUMBER}"; else echo -n "${VERSION_RC}"; fi', returnStdout: true)}"
                }
                steps {
                    echo "Building version : ${VERSION} with suffix ${VERSION_SUFFIX}"
                }
            }

            stage('Test') {
                steps {
                    echo 'Conduct Testing'
                }
            }

            stage('Publish') {
                when {
                    expression { return params.RC }
                }

                steps {
                    echo "Complete Published"
                }
            }
        }
    }
}