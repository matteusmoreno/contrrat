# --- Estágio de Build ---
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests

# --- Estágio Final ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# A variável de ambiente TZ não é mais necessária aqui, pois vamos passar diretamente para o Java.
# ENV TZ=America/Sao_Paulo

EXPOSE 8686
COPY --from=builder /app/target/contrrat-0.0.1-SNAPSHOT.jar ./app.jar

# --- INÍCIO DA CORREÇÃO ---
# Adicionamos o parâmetro -Duser.timezone=America/Sao_Paulo diretamente ao comando java
ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-jar", "app.jar"]
# --- FIM DA CORREÇÃO ---