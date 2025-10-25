
########## BUILD ##########
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# 1. Кладём только файлы сборки для кэша зависимостей
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# 2. Прогреваем зависимости
RUN ./mvnw -q -DskipTests dependency:go-offline

# 3. Копируем исходники и билдим
COPY src ./src
RUN ./mvnw -q -DskipTests package

########## RUNTIME ##########
FROM eclipse-temurin:21-jre
WORKDIR /app

# Копируем финальный jar
COPY --from=builder /app/target/*.jar /app/app.jar

# Порт HTTP, который слушает Spring Boot
EXPOSE 8080

# (опционально) можно сразу задать active profile, если хочешь
# ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java","-jar","/app/app.jar"]
