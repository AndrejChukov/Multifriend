package ru.multifriend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.chatId}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String text) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.telegram.org/bot" + botToken + "/sendMessage")
                .queryParam("chat_id", chatId)
                .queryParam("text", text)
                .queryParam("parse_mode", "Markdown")
                .build()
                .toUriString();

        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("Ошибка при отправке: " + e.getMessage());
        }
    }
}