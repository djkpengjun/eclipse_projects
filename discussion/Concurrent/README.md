rest-controller : problems with @EnableAspectJAutoProxy, @Aspect and @RestController. If the @Aspect wraps the controller, the dynamic proxy is not mapped

1 - The @Aspect is disabled, everyting is ok.

Go to the rest-controller dir
mvn clean install
java -jar target/rest-controller-0.0.1-SNAPSHOT.jar

wget http://localhost:8080/ctrl/process 
HTTP request sent, awaiting response... 200 OK

When the server starts, the logs shows the mapping for my controller :
s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/ctrl/process],methods=[],params=[],headers=[],consumes=[],produces=[application/json],custom=[]}" onto public java.lang.String test.MyController.process()

2 - Edit src/main/java/test/MyAspect.java to enable the @Aspect
Remove // before @Component (the Aspect will be detected by Spring)
mvn clean install
java -jar target/rest-controller-0.0.1-SNAPSHOT.jar

wget http://localhost:8080/ctrl/process
Status Code:404 Not Found

The server logs shows no more mapping for my controller and the previous request is failed.


3 - Use proxyTargetClass=true, in this case the proxy will contain the @RestController annotation and the mapping will be ok.
