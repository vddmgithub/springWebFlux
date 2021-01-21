FROM openjdk:11
COPY target/product-api-annotation-0.1.jar demo.jar
CMD ["java", "-jar", "/demo.jar"]
