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
                auditTools()
            }            
        }
        stage('Build') {
            environment {
                VERSION_SUFFIX = getVersionSuffix()
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

String getVersionSuffix() {
    if (params.RC) {
        return env.VERSION_RC
    }

    return env.VERSION_RC + ' ci. ' + env.BUILD_NUMBER
}

void auditTools() {
    sh '''
        git version
    '''
}