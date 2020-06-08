import groovy.json.JsonSlurper
// This Jenkinsfile is used by Jenkins to run the FireworksLayout step of Reactome's release.
// It requires that the DiagramConverter step has been run successfully before it can be run.
def currentRelease
def fireworksFolder = "fireworks"
pipeline{
	agent any

	stages{
		// This stage checks that upstream project DiagramConverter was run successfully.
		stage('Check DiagramConverter build succeeded'){
			steps{
				script{
					currentRelease = (pwd() =~ /Releases\/(\d+)\//)[0][1];
					// This queries the Jenkins API to confirm that the most recent build of DiagramConverter was successful.
					def diagramUrl = httpRequest authentication: 'jenkinsKey', validResponseCodes: "${env.VALID_RESPONSE_CODES}", url: "${env.JENKINS_JOB_URL}/job/File-Generation/job/DiagramConverter/lastBuild/api/json"
					if (diagramUrlUrl.getStatus() == 404) {
						error("DiagramConverter has not yet been run. Please complete a successful build.")
					} else {
						def diagramJson = new JsonSlurper().parseText(diagramUrl.getContent())
						if (diagramJson['result'] != "SUCCESS"){
							error("Most recent DiagramConverter build status: " + diagramJson['result'] + ". Please complete a successful build.")
						}
					}
				}
			}
		}
		// This stage builds the jar file using maven.
		stage('Setup: Build jar file'){
			steps{
				script{
					sh "mvn clean package"
				}
			}
		}
		// Execute the jar file, producing the diagram JSON files.
		stage('Main: Run Fireworks-Layout'){
			steps{
				script{
					sh "mkdir ${fireworksFolder}"
					withCredentials([usernamePassword(credentialsId: 'neo4jUsernamePassword', passwordVariable: 'pass', usernameVariable: 'user')]){
						sh "java -jar target/fireworks-jar-with-dependencies.jar --user $user --password $pass --output ./${fireworksFolder}"
					}
				}
			}
		}
		/*
		// Archive everything on S3, and move the 'diagram' folder to the download/vXX folder.
		stage('Post: Archive Outputs'){
			steps{
				script{
					def s3Path = "${env.S3_RELEASE_DIRECTORY_URL}/${currentRelease}/diagram_converter"
					def diagramArchive = "diagrams-v${currentRelease}.tgz"
					sh "tar -zcvf ${diagramArchive} ${diagramFolder}"
					sh "mv ${diagramFolder} ${env.ABS_DOWNLOAD_PATH}/${currentRelease}/" 
					sh "gzip reports/*"
					sh "aws s3 --no-progress cp ${diagramArchive} $s3Path/"
					sh "aws s3 --no-progress --recursive cp reports/ $s3Path/reports/"
					sh "rm -r ${diagramArchive} reports"
				}
			}
		}
		*/
	}
}
