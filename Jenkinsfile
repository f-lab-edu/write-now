pipeline{
    agent any

    stages{
        stage('github clone'){
            steps {
                git branch: 'master', credentialsId: 'github_auth', url: "https://github.com/f-lab-edu/write-now.git"
            }
        }
        stage('build'){
            steps{
                sh'''
                echo 'start bootJar'
                chmod +x gradlew
                ./gradlew clean bootjar
                '''
            }
        }
        stage('deploy'){
            steps{
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'ssh',
                            transfers: [
                                sshTransfer(cleanRemote: false,
                                excludes: '',
                                execCommand: 'sh /deploy/test.sh',
                                execTimeout: 120000,
                                flatten: false,
                                makeEmptyDirs: false,
                                noDefaultExcludes: false,
                                patternSeparator: '[, ]+',
                                remoteDirectory: '/',
                                remoteDirectorySDF: false,
                                removePrefix: 'build/libs',
                                sourceFiles: 'build/libs/*.jar')
                            ],
                            usePromotionTimestamp: false,
                            useWorkspaceInPromotion: false,
                            verbose: false
                        )
                    ]
                )
            }
        }
    }
}