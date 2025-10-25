package ru.glebpad.axiomustelegramapi.controller;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.glebpad.axiomustelegramapi.service.TelegramService;

@Component
public class TelegramController extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramController.class);
    private final String botUsername;
    private final TelegramService telegramService;

    public TelegramController(
            @Value("${telegram.bot.token}") String token,
            @Value("${telegram.bot.username}") String botUsername,
            TelegramService telegramService
    ) {
        super(token);
        this.botUsername = botUsername;
        this.telegramService = telegramService;
    }

    @PostConstruct
    public void register() throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
        log.info("Telegram bot registered as @{}", botUsername);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            SendMessage sendMessage = telegramService.handleRequest(update);
            super.execute(sendMessage);
        } catch (Exception e) {
            log.error("Error while processing update {}", update, e);
        }
    }
}