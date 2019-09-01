import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class GifmagesBot extends TelegramLongPollingBot {
    private static final String TOKEN = "972751462:AAHxxb4soOb-ALh76qm95odFXPR2W_ZKDhA";
    private static final String BOT_NAME = "Gifmagesbot";
    private List<String> data = new ArrayList<>();
    private String CURRENT_REQUEST = null;
    private String CURRENT_STRING_REQUEST = null;
    private int CURRENT_PAGE = 0;
    private int CURRENT_ITEM = 0;

    protected GifmagesBot(DefaultBotOptions defaultBotOptions){
        super(defaultBotOptions);
    }
    protected GifmagesBot(){}
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            processMessage(update);
        } else if (update.hasCallbackQuery()) {
            processCallback(update);
        } else
            sendMessage(update, "Something went wrong");
    }

    private void processCallback(Update update) {
        CallbackQuery callback = update.getCallbackQuery();

        if (!callback.getData().isEmpty()) {
            switch (callback.getData()) {
                case "Image":
                    CURRENT_REQUEST = "Image";
                    sendMessage(update, "Enter Request");
                    break;
                case "Gif":
                    CURRENT_REQUEST = "Gif";
                    sendMessage(update, "Enter Request");
                    break;
                case "More":
                    if (CURRENT_REQUEST.equals("Image")) {
                        CURRENT_ITEM++;
                        if (data.size() <= CURRENT_ITEM) {
                            //Getting new Images
                            CURRENT_PAGE++;
                            data = new ImageClient().getImage(CURRENT_STRING_REQUEST, CURRENT_PAGE);
                            if (data.size() > 0) {
                                CURRENT_ITEM = 0;
                                String[] buttonsName = {"Gif or Image", "More"};
                                String[] buttonsCallback = {"Change", "More"};
                                InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
                                sendPhoto(update, data.get(CURRENT_ITEM), keyboardMarkup);
                            } else {
                                sendMessage(update, "Unfortunately, there're no more Images, try to change request");
                            }
                        } else {
                            //Sending next image
                            String[] buttonsName = {"Gif or Image", "More"};
                            String[] buttonsCallback = {"Change", "More"};
                            InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
                            sendPhoto(update, data.get(CURRENT_ITEM), keyboardMarkup);
                        }
                    } else if (CURRENT_REQUEST.equals("Gif")) {
                        CURRENT_ITEM++;
                        if (data.size() <= CURRENT_ITEM) {
                            //Getting new gifs
                            CURRENT_PAGE++;
                            data = new ImageClient().getImage(CURRENT_STRING_REQUEST, CURRENT_PAGE);
                            if (data.size() > 0) {
                                CURRENT_ITEM = 0;
                                String[] buttonsName = {"Gif or Image", "More"};
                                String[] buttonsCallback = {"Change", "More"};
                                InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
                                sendAnimation(update, data.get(CURRENT_ITEM), keyboardMarkup);
                            } else {
                                sendMessage(update, "Unfortunately, there're no more Gifs, try to change request");
                            }
                        } else {
                            //Sending next gif
                            String[] buttonsName = {"Gif or Image", "More"};
                            String[] buttonsCallback = {"Change", "More"};
                            InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
                            sendAnimation(update, data.get(CURRENT_ITEM), keyboardMarkup);
                        }
                    } else {
                        String[] buttonsName = {"Image", "Gif"};
                        String[] buttonsCallback = {"Image", "Gif"};
                        InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
                        sendMessage(update, "Choose Image or Gif", keyboardMarkup);
                    }
                    break;

                case "Change":
                    String[] buttonsName = {"Image", "Gif"};
                    String[] buttonsCallback = {"Image", "Gif"};
                    InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
                    sendMessage(update, "Image or Gif", keyboardMarkup);
                    break;
            }
        }
    }


    private void processMessage(Update update) {
        String message = update.getMessage().getText();
        if (message.startsWith("/start")) {
            System.out.println(update.getMessage().getChatId());
            sendMessage(update, "Hello and welcome to Gifmages bot. " +
                    "This bot is made to help people finding Images and Gifs. Hope, You will enjoy it");
            //Sending first message Gif or Image
            String[] buttonsName = {"Image", "Gif"};
            String[] buttonsCallback = {"Image", "Gif"};
            InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
            sendMessage(update, "Image or Gif", keyboardMarkup);
        } else if (!message.isEmpty())
            //Handling with request if it can be handled)
            switch (CURRENT_REQUEST) {
                case ("Image"):
                    handleImageRequest(update);
                    break;
                case ("Gif"):
                    handleGifRequest(update);
                    break;
                default:
                    String[] buttonsName = {"Image", "Gif"};
                    String[] buttonsCallback = {"Image", "Gif"};
                    InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
                    sendMessage(update, "You didn't choose what you want :D", keyboardMarkup);
            }
    }

    private InlineKeyboardMarkup createKeyboard(String[] buttonsName, String[] buttonsCallback) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < buttonsName.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton()
                    .setText(buttonsName[i])
                    .setCallbackData(buttonsCallback[i]);
            row.add(button);
        }
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    private void handleImageRequest(Update update) {

        CURRENT_STRING_REQUEST = update.getMessage().getText();
        data = new ImageClient().getImage(CURRENT_STRING_REQUEST);

        if (data.size() > 0) {
            CURRENT_ITEM = 0;
            CURRENT_PAGE = 1;
            CURRENT_REQUEST = "Image";
            String[] buttonsName = {"Gif or Image", "More"};
            String[] buttonsCallback = {"Change", "More"};
            InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
            sendPhoto(update, data.get(CURRENT_ITEM), keyboardMarkup);
        } else {
            sendMessage(update, "Bad request, repeat pls");
        }

    }

    private void handleGifRequest(Update update) {

        CURRENT_STRING_REQUEST = update.getMessage().getText();
        data = new GifClient().getGif(CURRENT_STRING_REQUEST);

        if (data.size() > 0) {
            CURRENT_ITEM = 0;
            CURRENT_PAGE = 0;
            CURRENT_REQUEST = "Gif";
            String[] buttonsName = {"Gif or Image", "More"};
            String[] buttonsCallback = {"Change", "More"};
            InlineKeyboardMarkup keyboardMarkup = createKeyboard(buttonsName, buttonsCallback);
            sendAnimation(update, data.get(CURRENT_ITEM), keyboardMarkup);
        } else {
            sendMessage(update, "Bad request, repeat please");
        }
    }

    private void sendMessage(Update update, String text) {
        long chatId = getChatFromUpdate(update).getId();
        try {
            execute(new SendMessage().setText(text).setChatId(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Update update, String text, InlineKeyboardMarkup keyboardMarkup) {
        long chatId = getChatFromUpdate(update).getId();
        try {
            execute(new SendMessage().setText(text).setChatId(chatId).setReplyMarkup(keyboardMarkup));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(Update update, String photoUrl) {
        long chatId = getChatFromUpdate(update).getId();
        try {
            execute(new SendPhoto().setPhoto(photoUrl).setChatId(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(Update update, String photoUrl, InlineKeyboardMarkup keyboardMarkup) {
        long chatId = getChatFromUpdate(update).getId();
        try {
            sendMessage(update, "Downloaded from pixabay.com");
            execute(new SendPhoto().setPhoto(photoUrl).setChatId(chatId).setReplyMarkup(keyboardMarkup));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAnimation(Update update, String gifUrl, InlineKeyboardMarkup keyboardMarkup) {
        long chatId = getChatFromUpdate(update).getId();
        try {
            execute(new SendAnimation().setAnimation(gifUrl).setChatId(chatId).setReplyMarkup(keyboardMarkup));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    private static Chat getChatFromUpdate(Update update) {
        return update.getMessage() != null ? update.getMessage().getChat()
                : update.getCallbackQuery().getMessage().getChat();
    }

}
