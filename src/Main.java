import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
//        defaultBotOptions.setProxyHost("51.75.127.238");
//        defaultBotOptions.setProxyPort(3128);
//        defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        try{
            telegramBotsApi.registerBot(new GifmagesBot());
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
