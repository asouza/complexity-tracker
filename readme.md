# Why?

This is just a pet project which tries to show how complexity increases
through time for the most of projects. 

## Language support

At this this time only java project can be analyzed.

## How to run

### phase 1 
* Clone the project
* You should have java and mysql installed
* Clone https://github.com/mauricioaniche/ck
  * After cloned, you need to execute mvn install --skipTests
* Go to application.properties and change username and password :P
* Run com.deveficiente.complexitytracker.ComplexityTrackerApplication
* Send a post using form-url-encoded for http://localhost:8080/generate-history with:
  * startDate - start of the range of commits. yyyy-MM-dd
  * endDate - end of the range of commits. yyyy-MM-dd
  * projectId - identifier that you choose for the project being imported
  * localGitPath - path to local git repo
  * javaFilesFolderPath - you need choose which package you want to analyze. Ex: src/main/java/org/jasig/ssp/service/impl
* Each time you import a project with some id, the older version with the same id is deleted  
* After run, a 201 status should be returned with the first report url.
	* Ex: http://localhost:8080/reports/pages/complexity-by-class%3FprojectId=ssp
	* Change %3f for ? and go to the url 
	
The suggestion here is to pick a small range. The complexy history will be
showed ordered by class with higher loc(line of code). After that, I suggest
you to analyze each of the classes in a larger(probably there is a better word here) range.

### phase 2	

* Send a post using form-url-encoded for http://localhost:8080/generate-history with:
  * startDate - start of the range of commits. yyyy-MM-dd
  * endDate - end of the range of commits. yyyy-MM-dd
  * projectId - identifier that you choose for the class of the project being imported
  * localGitPath - path to local git repo
  * simpleClassName - a simple name of a class. This name will be used to filter all classes starting with it.
* Each time you import history of a project with some id, the older version with the same id is deleted  
* After run, a 201 status should be returned with the first report url.
	* Ex: http://localhost:8080/reports/pages/complexity-by-class%3FprojectId=ssp
	* Change %3f for ? and go to the url  

Now I suggest you to pick a different projectId and a larger() range	 
  