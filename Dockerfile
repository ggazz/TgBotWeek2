# Используем легковесный образ с OpenJDK 11
FROM openjdk:11-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/telegram-bot-1.0.0-SNAPSHOT-jar-with-dependencies.jar /app/bot.jar

# Устанавливаем переменную окружения (можно переопределить при запуске)
ENV BOT_TOKEN=your_bot_token_here

# Запускаем бота
CMD ["java", "-jar", "/app/bot.jar"]