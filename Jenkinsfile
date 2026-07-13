// pipeline{
//     agent any
//     stages{
//         stage('Checkout'){
//             steps{
//                 checkout scm
//                 // this makes it reusbale accreos repos , no hard git cmd
//             }
//         }
//         stage('Build'){
//             steps{
//                 sh './mvnw clean compile'
//                 // jenkins runs in a linux sheel that why sh
//             }
//         }
//         stage('Test'){
//             steps{
//                 sh './mvnw test'
//             }
//         }
//     }
//     post{
//         always{
//             echo 'Pipeline Finished.'
//         }
//         success{
//             echo 'Build Successful!'
//         }
//         failure{
//             echo 'Build Failed'
//         }
//     }
// }
pipeline {

    agent any

    stages {

        stage('Environment') {
            steps {
                sh 'pwd'
                sh 'ls -la'
                sh 'java -version'
                sh 'chmod +x mvnw'
                sh './mvnw -version'
            }
        }

    }

}