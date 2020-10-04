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

        stage ('Package') {
            agent {
                docker {
                    reuseNode true
                    image 'maven:3-alpine'
                    args '-v $HOME/.m2:/root/.m2 -u jenkins:jenkins'
                }
            }

            steps {
                sh 'mkdir -p boot-project'
                dir('boot-project') {
                    git branch: 'develop',
                    url: 'https://github.com/wissensalt/readable-mess-word'

                    sh '''
                        pwd
                        ls -l

                        mvn clean package
                    '''
                }
            }
        }

        stage ('Build and Test') {
            environment {
                VERSION_SUFFIX = versionSuffix()
                VERSION_SUFFIX2 = versionSuffix2 rcNumber: env.VERSION_RC, isReleaseCandidate: params.RC
            }

            steps {
                echo "Building version ${VERSION} with SUFFIX ${VERSION_SUFFIX}"                
            
                dir('boot-project') {
                    sh '''
                        pwd
                        ls -l

                        chmod +x build.sh
                        ./build.sh
                    '''
                }
            }    
        }        

        stage ('Run') {
            agent {
                docker {
                    reuseNode true
                    image 'maven:3-alpine'
                    args '-v $HOME/.m2:/root/.m2 -u 0:0'
                }
            }
            
            steps {
                dir('boot-project') {
                    sh '''                    
                        chmod +x run.sh                    
                        ./run.sh
                    '''
                }

                mstest testResultFile:"**/*.trx", keepLongStdio: true
            }
        }
    }
}