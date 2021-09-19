# SiteDownloader
Implementation of a code assignment. 
This application will take a web pgae, traverse the html and fetch all the defined links, and save the pages to the system.

The output of the application will be saved under /out, with a folder structure matching the url of the web pages.

## Run the application

Run with maven using CLI:
            
    mvn spring-boot:run
    
The program will recursively fetch and download all the relative links of a web page. 
The depth of the search is set in the application properties. To change the depth of the search update the property forkPoolThreshold in the application.yaml. 
    
## Build a runnable JAR file

Build the application using maven and package the program to a runnable JAR-file:

    mvn clean install
    mvn package
    
The JAR-file can then be run using a Java command:

    java -jar target/SiteDownloader-0.0.1-SNAPSHOT.jar

The output of the program will be found in the folder out/ as defined in the application properties.

## Tests

Run tests with maven using CLI:
    
    mvn test