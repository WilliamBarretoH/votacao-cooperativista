
# Etapa 1: build usando Gradle com Java 17
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Realiza o build da aplicação gerando o bootJar
RUN gradle clean bootJar --no-daemon

# Etapa 2: imagem final apenas com o jar
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o jar construído na etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Expõe a porta padrão da aplicação Spring Boot
EXPOSE 8080

# Comando para rodar o app
ENTRYPOINT ["java", "-jar", "app.jar"]
