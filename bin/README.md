# gjapp
First step:

Open file /src/application.properties, check similar bellow code:
spring.datasource.url=jdbc:mariadb://localhost/db_name?user=root&password=123

First of all, create database name [db_name], sure your user and pass is correct. If your user has no pass, just remove "&password=123".
That's all.

Import to eclipse as Maven project.

Update maven.

Run on tomcat >= 7.

Commnad build .war file:

mvn clean package
