# syntax=docker/dockerfile:1

########## BUILD ##########
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

# Кладём только файлы сборки для кэша зависимостей
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Прогрев зависимостей оффлайн
RUN ./mvnw -q -DskipTests dependency:go-offline

# Теперь исходники и сборка
COPY src ./src
RUN ./mvnw -q -DskipTests package

########## RUNTIME ##########
FROM eclipse-temurin:25-jre
WORKDIR /app

# Если артефакт один, можно схватить *.jar
# Лучше явно указать имя, если знаешь.
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
