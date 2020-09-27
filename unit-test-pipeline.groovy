library identifier: 'jenkins-pipeline-library@develop',
        retriever: modernSCM([$class: 'GitSCMSource', remote: 'https://github.com/wissensalt/jenkins-pipeline-library.git'])

pipeline {
    agent any

    environment {
        VERSION = "1.0.0"
        VERSION_RC = "RC.2"
    }

    stages {
        stage ('Audit Tools') {
            steps {
                auditTools()
            }
        }

        stage ('Build') {
            environment {
                VERSION_SUFFIX = versionSuffix()
                VERSION_SUFFIX2 = versionSuffix2 rcNumber: env.VERSION_RC, isReleaseCandidate: params.RC
            }

            steps {
                echo "Building version ${VERSION} with SUFFIX ${VERSION_SUFFIX}"                

                sh 'mkdir -p boot-project'
                dir('boot-project') {
                    git branch: 'master',
                    credentialsId: '4b2b3bb7-b857-49e5-96bb-583e77230828',
                    url: 'https://github.com/wissensalt/readable-mess-word'

                    sh 'pwd'
                    sh 'ls -l'                    
                }

                dir('${env.WORKSPACE}/boot-project/readable-mess-word') {
                    sh '''
                        pwd
                        ls -l
                        
                        chmod +x build.sh
                        chmod +x run.sh

                        ./build.sh
                        ./run.sh
                    '''
                }
            }    
        }        

        stage ('Test') {
            steps {
                sh '''
                    cd boot-project/readable-mess-word
                    mvn test
                '''

                mstest testResultFile:"**/*.trx", keepLongStdio: true
            }
        }
    }
}