# Usa una imagen base de Amazon Linux 2
FROM amazonlinux:2 AS build

# Instala Java 17 y otras herramientas necesarias
RUN yum install -y \
    java-17-amazon-corretto-devel \
    git \
    && yum clean all

# Instala Maven Wrapper (si aun no esta incluido en tu proyecto)
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Copia los archivos del codigo fuente
COPY src ./src

# Da permisos de ejecucion al script mvnw
RUN chmod +x mvnw

# Compila el proyecto y empaqueta el jar usando Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Usa una imagen base ligera de Java 17 para la ejecucion
FROM amazoncorretto:17-alpine

# Establece el directorio de trabajo para el contenedor de ejecucion
WORKDIR /app

# Copia el jar desde la etapa de compilacion
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto 80
EXPOSE 80

# Comando para ejecutar la aplicacion
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
