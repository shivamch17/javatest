pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Checkout the repository
                git 'https://github.com/shivamch17/javatest.git'
            }
        }

        stage('Build') {
            steps {
                // Build Maven project
                bat 'mvn clean install'
            }
        }

        stage('Package Fat JAR') {
            steps {
                // Generate fat JAR using maven-assembly-plugin
                bat 'mvn assembly:single'
            }
        }

        stage('Run Application') {
            steps {
                // Run the generated fat JAR
                bat 'java -jar target/javatest-1.0-SNAPSHOT-jar-with-dependencies.jar'
            }
        }

        stage('Archive Artifact') {
            steps {
                // Archive the fat JAR as a build artifact
                archiveArtifacts artifacts: 'target/javatest-1.0-SNAPSHOT-jar-with-dependencies.jar', allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            echo 'Build succeeded! Run further deployment or notification steps here.'
        }
        failure {
            echo 'Build failed! Send notifications or take corrective actions here.'
        }
    }
}
