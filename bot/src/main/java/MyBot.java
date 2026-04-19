import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "Multi-Friend";
    }

    @Override
    public String getBotToken() {
        return "8547798294:AAGA-ySrSYoJc5cWvyaUrMGsmu3mayjj7gA";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Обработка текстовых команд
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equals("/start")) {
                sendMainMenu(update.getMessage().getChatId());
            }
        }
        // Обработка нажатий на кнопки
        else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            handleCallback(chatId, messageId, callbackData);
        }
    }

    private void handleCallback(long chatId, int messageId, String data) {
        String text = "";
        switch (data) {
            case "main_menu":
                updateMenu(chatId, messageId, "Выберите действие:", getMainMenuKeyboard());
                return;
            case "services":
                text = "Услуги";
                break;
            case "reviews":
                text = "Ссылки (Отзывы)";
                break;
            case "portfolio":
                text = "Примеры работ";
                break;
            case "order":
                text = "Запись на услуги";
                break;
        }
        updateMenu(chatId, messageId, text, getBackKeyboard());
    }

    // Первый вход
    private void sendMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:");
        message.setReplyMarkup(getMainMenuKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    // Обновление существующего сообщения
    private void updateMenu(long chatId, int messageId, String text, InlineKeyboardMarkup keyboard) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        editMessage.setReplyMarkup(keyboard);
        try {
            execute(editMessage);
        } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    // Главное меню
    private InlineKeyboardMarkup getMainMenuKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(createRow("Услуги", "services"));
        rows.add(createRow("Отзывы", "reviews"));
        rows.add(createRow("Примеры работ", "portfolio"));
        rows.add(createRow("Заказать услугу", "order"));

        markup.setKeyboard(rows);
        return markup;
    }

    // Кнопка назад
    private InlineKeyboardMarkup getBackKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(createRow("Назад", "main_menu"));
        markup.setKeyboard(rows);
        return markup;
    }

    private List<InlineKeyboardButton> createRow(String text, String callbackData) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        row.add(button);
        return row;
    }
}