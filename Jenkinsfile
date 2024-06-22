pipeline {
    agent any
    tools {
        // Define Maven tool with the name configured in Global Tool Configuration
        git 'Default'
        maven 'Maven_System'
        jdk 'jdk'
    }
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

        stage('Run Application') {
            steps {
                // Run the generated fat JAR
                bat 'copy resources/Book1.xlsx target'
                bat 'cd target'
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
