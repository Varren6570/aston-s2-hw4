# === Этап 1: Сборка Java-приложения ===
FROM maven:3-eclipse-temurin-17 AS build

# Установка рабочей директории для сборки
WORKDIR /usr/src/app

# Копирование файла pom.xml и предварительная загрузка зависимостей
COPY pom.xml ./
RUN mvn dependency:go-offline

# Копирование исходного кода проекта
COPY . .

# Сборка проекта с пропуском модульных тестов
RUN mvn clean package -DskipTests


# === Этап 2: Создание финального образа с JRE ===
FROM eclipse-temurin:17-jre-jammy

# Установка рабочей директории для выполнения
WORKDIR /app

# Объявление аргумента с именем jar-файла
# !!!Заменить значение JAR_FILE на настоящее имя приложения!!!
ARG JAR_FILE=MainApplication

# Копирование собранного jar-файла из предыдущего этапа и переименование в app.jar
COPY --from=build /usr/src/app/target/${JAR_FILE}.jar /app/app.jar

# Открытие порта для входящих соединений (если необходимо)
EXPOSE 8081

# Определение точки входа — запуск jar-файла
ENTRYPOINT ["java", "-jar", "app.jar"]
