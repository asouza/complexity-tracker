# Why?

This is just a pet project which tries to show how complexity increases
through time for the most of projects. 

## Language support

At this this time only java project can be analyzed.

## How to run

* Clone the project
* You should have java and mysql installed
* Clone https://github.com/mauricioaniche/ck
  * After cloned, you need to execute mvn install --skipTests
* Go to application.properties and change username and password :P
* Run com.deveficiente.complexitytracker.ComplexityTrackerApplication
* Send a post for http://localhost:8080/generate-history with:
  * commitHashes - commit hashes splited by commas. Ex: f74be96dada91d6d15cc7c3954050e4133de16bf,5ab12e1ffdff4254f164c0d963661af9db5f0d9e
  * projectId - identifier that you choose for the project being imported
  * localGitPath - path to local git repo
  * javaFilesFolderPath - you need choose which package you want to analyze. Ex: src/main/java/org/jasig/ssp/service/impl
* Each time you import a project with some id, the older version with the same id is deleted  
* After run, a 201 status should be returned with the first report url.
	* Ex: http://localhost:8080/reports/pages/complexity-by-class%3FprojectId=ssp
	* Change %3f for ? and go to the url 
 
  