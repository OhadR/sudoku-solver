sudoku-solver
=============

sudoku solver, the engine is in Java, and it communicates with the front-end (UI) via a Java Servlet, that bridges the engine to a JS and HTML.

This is an AppEngine Web application. so it is ready for deployment to AppEngine.

my version is at: http://sudoku-solvr.appspot.com/

project brief: http://www.youtube.com/watch?v=_Fzs5qfVzQc

# Maven

I have added support with Maven is a separate branch. The layout of the project is not [Maven's standard](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html) so I had to add a specific configuration to adapt (see pom.xml)

## Debug within Eclipse

As maven support added, I have also added *tomcat7-maven-plugin*, so it is easy to debug this app with eclipse. See how in this README: https://gitlab.com/OhadR/activemq-spring-sandbox#debug-within-eclipse
