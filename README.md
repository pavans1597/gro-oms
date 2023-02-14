# quality-management-service

Steps to convert bootstrap project to your service project with multitenancy before importing project in your IDE

1. Clone or pull `quality-management-service` on your system

2. Copy the bootstrap service to new folder for your application using -

   `cp -R quality-management-service your_service-project`

3. Now go to `your_service-project` using `cd your_service-project` and run following commands -
```
	git init
	
	Edit the file vim .git/config and update the remote URL to
	git remote add origin https://github.com/user/your_service.git
```

4. Edit the `pom.xml`
````
   a. Change `bootstrap-multitenant-service-parent` to `your_service-parent` in `artifactId` and `name` (top of file)
   b. Change `bootstrap-multitenant-service` to `your_service` in `module` (at the center of file)
   c. Change profile id from `bootstrap-multitenant-service` to `your_service-project` (at the bottom of file)
   d. Save and exit
````
5. Now rename the bootstrap folder to your_service folder using -

   `mv bootstrap-multitenant-service your_service`

6. Go to renamed folder `your_service` and edit `pom.xml`
```
	a. Change `bootstrap-multitenant-service` to `your_service` in `artifactId` and `name` and update the description as well (top of file)
	b. Change `bootstrap-multitenant-service-parent` to `your_service-parent` in `artifactId` in parent tag (top of file)
	c. Change `bootstrap-multitenant-service` to `your_service` in `finalName` in `build` plugin (at the bottom of file)
```

7. Import the `your_service-project` in your IDE

8. Rename the package `com.groyyo.bootstrapMultiTenantService` to `com.groyyo.your_project` and organize imports

9. Rename `BootstrapMultiTenantServiceApplication.java` to `YourAppNameApplication.java`

10. Change basePackages for repository and entity in `DatabaseConfig.java`

11. Update `context-path`, `application-name`, access log file name, database name, kafka client id, kafka group id in `application.properties`

12. Update log file name in `log4j2.xml`

13. Commit the code.

14. Create a folder /var/log/groyyo (if not present)

15. sudo chmod 777 /var/log/groyyo

Note - You may view `https://github.com/groyyo/bootstrap-multitenant-service` to see how this bootstrap multitenant project is implemented.
