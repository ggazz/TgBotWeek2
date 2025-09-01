package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.response.GetMeResponse;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WeekColorBot {
    private static final String FIRST_WEEK_COLOR = "синяя";
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(WeekColorBot.class.getName());

    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");
        if (botToken == null || botToken.isEmpty()) {
            System.err.println("Ошибка: переменная окружения BOT_TOKEN не установлена!");
            System.exit(1);
        }
        TelegramBot bot = new TelegramBot(botToken);

        GetMeResponse getMeResponse = bot.execute(new GetMe());
        String botUsername = getMeResponse.user().username();
        if (botUsername == null) {
            System.err.println("Ошибка: не удалось получить имя бота!");
            System.exit(1);
        }
        LOGGER.info("Бот запущен с именем: " + botUsername);

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    long chatId = update.message().chat().id();
                    String messageText = update.message().text();

                    if (messageText.equalsIgnoreCase("/week") || messageText.equalsIgnoreCase("/week@" + botUsername)) {
                        String weekColor = getWeekColor();
                        bot.execute(new SendMessage(chatId, "Сегодня " + weekColor + " неделя."));
                    } else {
                        bot.execute(new SendMessage(chatId, "Отправьте /week, чтобы узнать цвет недели."));
                    }
                }

                if (update.message() != null && update.message().newChatMembers() != null) {
                    for (com.pengrad.telegrambot.model.User newMember : update.message().newChatMembers()) {
                        if (newMember.isBot() && newMember.username().equals(botUsername)) {
                            long chatId = update.message().chat().id();
                            bot.execute(new SendMessage(chatId, "Привет! Я бот, который сообщает, какая сегодня неделя (синяя или красная). Используйте команду /week."));
                        }
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static String getWeekColor() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        long weeks = ChronoUnit.WEEKS.between(startDate, today);
        boolean isFirstWeekBlue = FIRST_WEEK_COLOR.equalsIgnoreCase("синяя");
        boolean isEvenWeek = weeks % 2 == 0;
        return (isFirstWeekBlue == isEvenWeek) ? "синяя" : "красная";
    }
}