"C:\Program Files\Java\jre1.8.0_201\bin\java" -jar -Dspring.config.location=classpath://application-apiclient.yml -Dapplication.name=cacherapigateway -Dspring.profiles.active=noauth -Dserver.port=8087 -Dcache.api.port=8088 -Deureka.port=8082 -Dcacher.name=cacher1 -Dcacher1.name=CACHER1 -Dcacher2.name=CACHER2 cacher-0.0.1-SNAPSHOT.jar