# Usa una imagen base de Amazon Linux 2
FROM amazonlinux:2 AS build

# Instala Java 17 y otras herramientas necesarias
RUN yum install -y \
    java-17-amazon-corretto-devel \
    git \
    tar \
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
COPY --from=build /target/*.jar app.jar

# Declarar un argumento para la IP
ARG MY_IP

# Establecer la variable de entorno a partir del argumento
ENV MY_IP=${MY_IP}

# Usar el argumento en un comando para verificarlo
RUN echo "La dirección IP es: $MY_IP" > /app/my_ip.txt

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicacion
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
