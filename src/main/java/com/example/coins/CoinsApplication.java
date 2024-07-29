package com.example.coins;

import com.example.coins.config.BotConfig;
import com.example.coins.model.User;
import com.example.coins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CoinsApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BotConfig botConfig;

    public static void main(String[] args) {
        SpringApplication.run(CoinsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyTelegramBot(botConfig, userRepository));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static class MyTelegramBot extends TelegramLongPollingBot {

        private final UserRepository rep;
        private final BotConfig botConfig;

        public MyTelegramBot(BotConfig botConfig, UserRepository rep) {
            this.botConfig = botConfig;
            this.rep = rep;
        }

        @Override
        public String getBotUsername() {
            return "YourBotUsername"; // Replace with your bot's username
        }

        @Override
        public String getBotToken() {
            return botConfig.getBotToken();
        }

        @Override
        public void onUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();

                if (messageText.startsWith("/start")) {
                    handleStartCommand(update.getMessage());
                    sendWebAppMessage(chatId);
                } else {
                    sendMessage(chatId, "Received your message");
                }
            }
        }

        private void sendWebAppMessage(long chatId) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Открой Web-приложение");

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            button.setText("Открыть Web-приложение");
            button.setWebApp(new WebAppInfo(botConfig.getWebAppUrl()));
            row.add(button);
            keyboard.add(row);
            keyboardMarkup.setKeyboard(keyboard);
            message.setReplyMarkup(keyboardMarkup);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        private void handleStartCommand(Message msg) {
            long userId = msg.getFrom().getId();
            String[] parts = msg.getText().split(" ");
            Integer referrerCandidate = null;
            List<User> users = rep.findAll();
            if (rep.findByTgId(userId).isPresent()) {
                return;
            }
            if (parts.length > 1) {
                try {
                    referrerCandidate = Integer.parseInt(parts[1]);
                    if (userId != referrerCandidate && rep.findByTgId(referrerCandidate).isPresent()) {
                        User user = new User();
                        user.setTgId(userId);
                        user.setRefId(referrerCandidate);
                        rep.save(user);
                        return;
                    }
                        User user = new User();
                        user.setTgId(userId);
                        user.setRefId(0);
                        rep.save(user);

                } catch (NumberFormatException e) {

                }
            }
        }

        private void sendMessage(long chatId, String text) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
