# Stage 1: Build
FROM maven:3.8.6-openjdk-11 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: Run
FROM eclipse-temurin:11.0.24_9-jre-slim
WORKDIR /app
COPY --from=builder /app/target/telegram-bot-1.0.0-SNAPSHOT-jar-with-dependencies.jar /app/bot.jar
ENV BOT_TOKEN=your_bot_token_here
CMD ["java", "-jar", "/app/bot.jar"]
