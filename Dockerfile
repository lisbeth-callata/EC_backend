# Usamos imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiamos todos los archivos
COPY . .

# Damos permisos de ejecución a mvnw
RUN chmod +x mvnw

# Compilamos con Maven
RUN ./mvnw clean package -DskipTests

# Exponemos el puerto
EXPOSE 8080

# Ejecutamos el JAR
ENTRYPOINT ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
