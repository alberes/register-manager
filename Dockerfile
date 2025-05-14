FROM maven:3.8.5-openjdk-17 as build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=build ./build/target/*.jar ./register-manager.jar

expose 8081

ENV DATASOURCE_URL=''
ENV DATASOURCE_USER=''
ENV DATASOURCE_PASSWORD=''
ENV VIA_CEP_URL='https://viacep.com.br/ws/'
ENV EXPIRATION_TIME=30

ENTRYPOINT java -jar register-manager.jar