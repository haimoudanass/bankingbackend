package com.yassirrijali.bankingbackend.ai.telegram;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramChatbotService extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramChatbotService.class);

    private final ChatClient chatClient;
    private final String botToken;
    private final String botUsername;

    public TelegramChatbotService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore) {

        this.botToken = botToken;
        this.botUsername = botUsername;

        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        Tu es l'assistant de la banque Digital Banking.
                        Reponds uniquement a partir du contexte documentaire fourni.
                        Si tu ne sais pas, dis : Je ne dispose pas de cette information.
                        Reponds en francais.
                        """)
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("Bot Telegram actif : @{}", botUsername);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Erreur enregistrement bot Telegram", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String userMessage = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        try {
            sendTypingAction(chatId);

            String aiResponse = chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();

            execute(new SendMessage(chatId.toString(), aiResponse));
        } catch (TelegramApiException e) {
            log.error("Erreur Telegram chatId={}", chatId, e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendTypingAction(Long chatId) throws TelegramApiException {
        SendChatAction action = new SendChatAction();
        action.setChatId(chatId.toString());
        action.setAction(ActionType.TYPING);
        execute(action);
    }
}
