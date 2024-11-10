pipeline {
    agent any

    tools {
        maven 'Maven'  // Make sure this matches your Jenkins tool name
        jdk 'JDK-17'         // Make sure this matches your Jenkins tool name
    }

    environment {
        DOCKER_REGISTRY = 'learnwithvinod'
        DOCKER_IMAGE = 'couchbase-demo-app'
        VERSION = "${BUILD_NUMBER}"
        SONAR_PROJECT_KEY = 'couchbase-demo-app'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

//         stage('Integration Tests') {
//             steps {
//                 sh 'mvn verify -DskipUnitTests'
//             }
//             post {
//                 always {
//                     junit '**/target/failsafe-reports/*.xml'
//                 }
//             }
//         }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'SonarScanner'
                    withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                        sh '''
                            mvn clean verify sonar:sonar
                        '''
                    }
                }
            }
        }

//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     docker.build("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${VERSION}")
//                 }
//             }
//         }
//
//         stage('Push Docker Image') {
//             steps {
//                 script {
//                     docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-registry-credentials') {
//                         docker.image("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${VERSION}").push()
//                         docker.image("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${VERSION}").push('latest')
//                     }
//                 }
//             }
//         }
//
    }

    post {
        always {
            // Clean workspace
            cleanWs()
        }
    }
}