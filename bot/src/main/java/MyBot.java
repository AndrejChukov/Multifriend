import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    private static final String ORDER_FORM_URL = "https://forms.gle/MLn6imNP8pJ5YMPm8";

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
        switch (data) {
            case "main_menu":
                this.updateMenu(chatId, messageId, "Главное меню", this.getMainMenuKeyboard());
                return;
            case "services", "services_categories":
                this.updateMenu(chatId, messageId, "Выберите категорию услуг:", this.getServicesCategoriesKeyboard());
                return;
            case "cat_cleaning":
                this.updateMenu(chatId, messageId, "Уборка территории\nВыберите конкретную услугу:", this.getCleaningServicesKeyboard());
                return;
            case "cat_lawn":
                this.updateMenu(chatId, messageId, "Уход за газоном\nВыберите конкретную услугу:", this.getLawnServicesKeyboard());
                return;
            case "cat_repair":
                this.updateMenu(chatId, messageId, "Ремонт и строительство\nВыберите конкретную услугу:", this.getRepairServicesKeyboard());
                return;
            case "service_snow":
                showServiceInfo(chatId, messageId, "snow");
                break;
            case "service_garbage":
                showServiceInfo(chatId, messageId, "garbage");
                break;
            case "service_landscape":
                showServiceInfo(chatId, messageId, "landscape");
                break;
            case "service_planting":
                showServiceInfo(chatId, messageId, "planting");
                break;
            case "service_mowing":
                showServiceInfo(chatId, messageId, "mowing");
                break;
            case "service_concrete":
                showServiceInfo(chatId, messageId, "concrete");
                break;
            case "service_fence":
                showServiceInfo(chatId, messageId, "fence");
                break;
            case "service_siding":
                showServiceInfo(chatId, messageId, "siding");
                break;
            case "reviews":
                this.updateMenu(chatId, messageId, "Отзывы\n\nВыберите категорию:", this.getServicesCategoriesKeyboard());
                return;
            case "reviews_cleaning":
                showReviews(chatId, messageId, "cleaning");
                return;
            case "reviews_lawn":
                showReviews(chatId, messageId, "lawn");
                return;
            case "reviews_repair":
                showReviews(chatId, messageId, "repair");
                return;
            case "portfolio":
                this.updateMenu(chatId, messageId, "Примеры работ\n\nВыберите категорию:", this.getPortfolioCategoriesKeyboard());
                return;
            case "portfolio_cleaning":
                sendPortfolioPhoto(chatId, messageId, "cleaning");
                return;
            case "portfolio_lawn":
                sendPortfolioPhoto(chatId, messageId, "lawn");
                return;
            case "portfolio_repair":
                sendPortfolioPhoto(chatId, messageId, "repair");
                return;
            default:
                updateMenu(chatId, messageId, "Команда не найдена", getBackKeyboard());
        }
    }

    // Первый вход
    private void sendMainMenu(long chatId) {
        try {
            SendMessage welcomeMessage = new SendMessage();
            welcomeMessage.setChatId(String.valueOf(chatId));
            welcomeMessage.setText("Поддержание порядка на частной территории требует много времени и сил.\n\n" +
                    "🏠 Команда Multi-friend предлагает:\n" +
                    "• Уборку территории\n" +
                    "• Уход за газоном\n" +
                    "• Обслуживание дома\n" +
                    "• Сезонные работы\n\n" +
                    "✨ Наслаждайтесь комфортом, а не тратьте выходные на тяжелую работу!");
            welcomeMessage.setParseMode("Markdown");
            this.execute(welcomeMessage);

            SendMessage menuMessage = new SendMessage();
            menuMessage.setChatId(String.valueOf(chatId));
            menuMessage.setText("Меню:");
            menuMessage.setParseMode("Markdown");
            menuMessage.setReplyMarkup(this.getMainMenuKeyboard());
            this.execute(menuMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
        rows.add(createUrlRow("Заказать услугу", ORDER_FORM_URL));

        markup.setKeyboard(rows);
        return markup;
    }

    // Меню категорий услуг
    private InlineKeyboardMarkup getServicesCategoriesKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList();
        rows.add(this.createRow("Уборка территории", "cat_cleaning"));
        rows.add(this.createRow("Уход за газоном", "cat_lawn"));
        rows.add(this.createRow("Ремонт и строительство", "cat_repair"));
        rows.add(this.createRow("Назад", "main_menu"));
        markup.setKeyboard(rows);
        return markup;
    }

    // Клавиатура для: Уборка территории
    private InlineKeyboardMarkup getCleaningServicesKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList();
        rows.add(this.createRow("Чистка снега", "service_snow"));
        rows.add(this.createRow("Вывоз мусора", "service_garbage"));
        rows.add(this.createRow("Облагораживание участка", "service_landscape"));
        rows.add(this.createRow("Назад", "services_categories"));
        markup.setKeyboard(rows);
        return markup;
    }

    // Клавиатура для: Уход за газоном
    private InlineKeyboardMarkup getLawnServicesKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList();
        rows.add(this.createRow("Засадка газона", "service_planting"));
        rows.add(this.createRow("Стрижка газона", "service_mowing"));
        rows.add(this.createRow("Назад", "services_categories"));
        markup.setKeyboard(rows);
        return markup;
    }

    // Клавиатура для: Ремонт и строительство
    private InlineKeyboardMarkup getRepairServicesKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList();
        rows.add(this.createRow("Заливка бетона", "service_concrete"));
        rows.add(this.createRow("Установка забора", "service_fence"));
        rows.add(this.createRow("Обшивка построек", "service_siding"));
        rows.add(this.createRow("Назад", "services_categories"));
        markup.setKeyboard(rows);
        return markup;
    }

    private void showServiceInfo(long chatId, int messageId, String serviceType) {
        String title = "";
        String description = "";
        String price = "";

        switch (serviceType) {
            case "snow":
                title = "Чистка снега";
                description = "Очистка территории от снега любого объема.\n\n" +
                        "• Уборка различных поверхностей\n" +
                        "• Вывоз снега (доп. услуга)\n" +
                        "• Обработка противогололедными материалами (доп услуга)";
                price = "Цена: от 500 руб/час";
                break;
            case "garbage":
                title = "Вывоз мусора";
                description = "Быстрый вывоз мусора с вашего участка.\n\n" +
                        "• Строительный, бытовой, крупногабаритный мусор\n" +
                        "• Собственный транспорт\n";
                price = "Цена: от 1000 руб/контейнер";
                break;
            case "landscape":
                title = "Облагораживание участка";
                description = "Преобразим ваш участок, сделав его уютным и красивым.\n\n" +
                        "• Планировка участка\n" +
                        "• Посадка деревьев и кустарников\n" +
                        "• Установка малых архитектурных форм";
                price = "Цена: по договоренности";
                break;
            case "planting":
                title = "Засадка газона";
                description = "Создадим идеальный газон любой сложности.\n\n" +
                        "• Подготовка почвы\n" +
                        "• Посев семян\n" +
                        "• Укладка рулонного газона";
                price = "Цена: от 500 руб/м²";
                break;
            case "mowing":
                title = "Стрижка газона";
                description = "Аккуратная стрижка газона с поддержанием формы.\n\n" +
                        "• Регулярная стрижка\n" +
                        "• Фигурная стрижка\n" +
                        "• Уборка скошенной травы";
                price = "Цена: от 300 руб/м²";
                break;
            case "concrete":
                title = "Заливка бетона";
                description = "Качественная заливка бетона для любых целей.\n\n" +
                        "• Отмостки, дорожки, площадки\n" +
                        "• Фундаменты\n" +
                        "• Бетонирование полов";
                price = "Цена: от 4000 руб/м³";
                break;
            case "fence":
                title = "Установка забора";
                description = "Установим забор любой сложности под ключ.\n\n" +
                        "• Профнастил, сетка, ковка\n" +
                        "• Дерево, бетон\n" +
                        "• Быстро и качественно";
                price = "Цена: от 2000 руб/м.п.";
                break;
            case "siding":
                title = "Обшивка построек";
                description = "Защитим и украсим ваши постройки.\n\n" +
                        "• Обшивка домов, бань, сараев\n" +
                        "• Сайдинг, блок-хаус, вагонка\n" +
                        "• Утепление по желанию";
                price = "Цена: от 800 руб/м²";
                break;
        }

        String fullText = String.format("*%s*\n\n%s\n\n %s\n\nДля записи нажмите /order",
                title, description, price);

        // Отправляем информацию с кнопкой "Назад к услугам"
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId(messageId);
        editMessage.setText(fullText);
        editMessage.setParseMode("Markdown");

        // Создаём клавиатуру с кнопкой записи и кнопкой назад
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Ряд с кнопкой-ссылкой на форму
        List<InlineKeyboardButton> urlRow = new ArrayList<>();
        InlineKeyboardButton urlButton = new InlineKeyboardButton();
        urlButton.setText("Записаться на эту услугу");
        urlButton.setUrl(ORDER_FORM_URL);
        urlRow.add(urlButton);
        rows.add(urlRow);

        rows.add(createRow("Назад к категориям", "services_categories"));

        keyboard.setKeyboard(rows);
        editMessage.setReplyMarkup(keyboard);

        try {
            this.execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Кнопка назад
    private InlineKeyboardMarkup getBackKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(createRow("Назад", "main_menu"));
        markup.setKeyboard(rows);
        return markup;
    }

    // Создание обычной кнопки
    private List<InlineKeyboardButton> createRow(String text, String callbackData) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        row.add(button);
        return row;
    }

    // Создание кнопки-ссылки (открывает URL при нажатии)
    private List<InlineKeyboardButton> createUrlRow(String buttonText, String url) {
        List<InlineKeyboardButton> row = new ArrayList();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setUrl(url);
        row.add(button);
        return row;
    }

    private InlineKeyboardMarkup getReviewsLinksKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Ряд со ссылками на отзывы
        List<InlineKeyboardButton> linksRow = new ArrayList<>();

        InlineKeyboardButton yandexButton = new InlineKeyboardButton();
        yandexButton.setText("Яндекс Отзывы");
        yandexButton.setUrl(""); // Пустая ссылка, потом вставить
        linksRow.add(yandexButton);

        InlineKeyboardButton dubleGISButton = new InlineKeyboardButton();
        dubleGISButton.setText("2ГИС Отзывы");
        dubleGISButton.setUrl(""); // Пустая ссылка, потом вставить
        linksRow.add(dubleGISButton);

        InlineKeyboardButton avitoButton = new InlineKeyboardButton();
        avitoButton.setText("Авито Отзывы");
        avitoButton.setUrl(""); // Пустая ссылка, потом вставить
        linksRow.add(avitoButton);

        rows.add(linksRow);

        rows.add(createRow("Назад", "reviews"));

        markup.setKeyboard(rows);
        return markup;
    }

    private void showReviews(long chatId, int messageId, String serviceType) {
        String title = "";
        String reviews = "";

        switch (serviceType) {
            case "cleaning":
                title = "Уборка территории";
                reviews = "Отзывы об уборке территории:\n\n" +
                        "Анна: «Заказала чистку снега зимой. Приехали быстро, всё убрали, даже крыльцо посыпали. Рекомендую!»\n\n" +
                        "Сергей: «Вывозили мусор после стройки. Приехали вовремя, загрузили всё аккуратно. Цена адекватная. Спасибо!»\n\n" +
                        "Елена: «Облагородили участок — посадили туи, разбили клумбу. Теперь двор как картинка!»";
                break;
            case "lawn":
                title = "Уход за газоном";
                reviews = "Отзывы об уходе за газоном:\n\n" +
                        "Дмитрий: «Засадили газон с нуля. Трава взошла ровно, зеленая и густая. Очень доволен!»\n\n" +
                        "Ольга: «Стригут газон раз в две недели. Всегда вовремя, аккуратно, траву увозят. Отличный сервис!»\n\n" +
                        "Игорь: «Помогли реанимировать старый газон. Сделали аэрацию, подсеяли траву. Теперь как новый!»";
                break;
            case "repair":
                title = "Ремонт и строительство";
                reviews = "Отзывы о ремонте и строительстве:\n\n" +
                        "Михаил: «Залили бетонную площадку под машину. Всё ровно, качественно, цена отличная!»\n\n" +
                        "Татьяна: «Установили забор из профнастила за 2 дня. Соседи уже тоже хотят такой!»\n\n" +
                        "Алексей: «Обшили баню вагонкой. Работают чисто, аккуратно, мусор вывезли. Буду заказывать ещё!»";
                break;
            default:
                return;
        }

        String fullText = title + "\n\n" + reviews + "\n\nБольше отзывов по ссылкам ниже:";

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId(messageId);
        editMessage.setText(fullText);
        editMessage.setParseMode("Markdown");
        editMessage.setReplyMarkup(getReviewsLinksKeyboard());

        try {
            this.execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getPortfolioCategoriesKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList();

        rows.add(createRow("Уборка территории", "portfolio_cleaning"));
        rows.add(createRow("Уход за газоном", "portfolio_lawn"));
        rows.add(createRow("Ремонт и строительство", "portfolio_repair"));
        rows.add(createRow("Назад", "main_menu"));

        markup.setKeyboard(rows);
        return markup;
    }

    // Пока оставлю так, потом надо будет доделать (добавить пути фото)
//    private void sendPortfolioPhoto(long chatId, int messageId, String category) {
//        String title = "";
//        String description = "";
//        String beforePhoto = "";
//        String afterPhoto = "";
//
//        switch (category) {
//            case "cleaning":
//                title = "Уборка территории";
//                description = "Уборка снега с территории частного дома";
//                beforePhoto = "cleaning_before.jpg";
//                afterPhoto = "cleaning_after.jpg";
//                break;
//            case "lawn":
//                title = "Уход за газоном";
//                description = "Стрижка газона после обработки";
//                beforePhoto = "lawn_before.jpg";
//                afterPhoto = "lawn_after.jpg";
//                break;
//            case "repair":
//                title = "Ремонт и строительство";
//                description = "Установка забора под ключ";
//                beforePhoto = "fence_before.jpg";
//                afterPhoto = "fence_after.jpg";
//                break;
//            default:
//                return;
//        }
//
//        // Удаляем старое сообщение с меню
//        DeleteMessage deleteMessage = new DeleteMessage();
//        deleteMessage.setChatId(String.valueOf(chatId));
//        deleteMessage.setMessageId(messageId);
//        this.execute(deleteMessage);
//
//        // Отправляем текст-описание
//        SendMessage descriptionText = new SendMessage();
//        descriptionText.setChatId(String.valueOf(chatId));
//        descriptionText.setText(String.format("*%s*\n\n%s\n\nРезультат работы:", title, description));
//        descriptionText.setParseMode("Markdown");
//        this.execute(descriptionText);
//
//        // Создаём альбом с двумя фото
//        List<InputMediaPhoto> mediaList = new ArrayList<>();
//
//        InputMediaPhoto before = new InputMediaPhoto();
//        before.setMedia(new InputFile(new File("src/main/resources/photos/" + beforePhoto)));
//        before.setCaption("До");
//        mediaList.add(before);
//
//        InputMediaPhoto after = new InputMediaPhoto();
//        after.setMedia(new InputFile(new File("src/main/resources/photos/" + afterPhoto)));
//        after.setCaption("После");
//        mediaList.add(after);
//
//        // Отправляем альбом
//        SendMediaGroup mediaGroup = new SendMediaGroup();
//        mediaGroup.setChatId(String.valueOf(chatId));
//        mediaGroup.setMedias(mediaList);
//        this.execute(mediaGroup);

//        SendMessage buttonsMsg = new SendMessage();
//        buttonsMsg.setChatId(String.valueOf(chatId));
//        buttonsMsg.setText("Больше отзывов по ссылкам ниже:");
//        buttonsMsg.setParseMode("Markdown");
//        buttonsMsg.setReplyMarkup(getReviewsLinksKeyboard());
//        this.execute(buttonsMsg);
//    }
}