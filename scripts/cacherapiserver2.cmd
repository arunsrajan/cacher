"C:\Program Files\Java\jre1.8.0_201\bin\java" -jar -Dspring.config.location=classpath://application.yml -Dapplication.name=CACHER1 -Dspring.profiles.active=cacherApi,noauth -Dserver.port=8085 -Deureka.port=8082 -Dcacher.name=cacher1 cacher-0.0.1-SNAPSHOT.jar