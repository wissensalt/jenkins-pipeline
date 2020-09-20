library identifier: 'jenkins-pipeline-library@develop',
        retriever: modernSCM([$class: 'GitSCMSource', remote: 'https://github.com/wissensalt/jenkins-pipeline-library.git'])
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
                VERSION_SUFFIX = versionSuffix()
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