FROM openjdk:21
COPY "./target/sigprodparcial-0.0.1-SNAPSHOT.jar" "app.jart"
EXPOSE 8217
ENTRYPOINT [ "java" , ".jar" , "app.jar" ]

