# Usamos Java 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiamos todos los archivos al contenedor
COPY . .

# Compilamos con Maven
RUN ./mvnw clean package -DskipTests

# Ejecutamos el JAR
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
