### Demo app to show issue with Quarkus context propagation
Problem: "Manual" context propagation does not work in native mode  
To read more about context propagation check [this article](https://quarkus.io/guides/context-propagation)

Steps to reproduce ht issue:
1. Run app in dev mode: `./mvnw quarkus:dev`
2. Get MDC values using `curl http://localhost:8080/uni && curl http://localhost:8080/thread-context`
Output:
```
MDC value:  from-uni
MDC value:  from-thread-context
```
3. Stop quarkus
4. Compile app into native executable: ` ./mvnw package -Pnative`
5. Start native app `./target/quarkus-context-propagation-1.0.0-SNAPSHOT-runner`
6. Get MDC values using the same command as in item 2: `curl http://localhost:8080/uni && curl http://localhost:8080/thread-context`
Output:
```
MDC value:  from-uni
MDC value:  null
```
