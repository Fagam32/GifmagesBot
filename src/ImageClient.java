import com.google.gson.Gson;
import jsonparsing.Hit;
import jsonparsing.JsonFile;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageClient {
    private static final String url1 = "https://pixabay.com/api/?key=13390764-73e8103915e6bf65433c7b376&q=";

    public List<String> getImage(String searchRequest, int page){
        StringBuilder sb = new StringBuilder();
        //Getting JSON File
        try{
            //PER PAGE = 10
            URL url = new URL(url1 + searchRequest + "&page=" + page + "&per_page=10");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            br.close();
        } catch (MalformedURLException e){
            System.out.println("Problems with URL");
        } catch (IOException e){
            System.out.println("Problems with opening connection");
        }

        //Parsing JSON File
        Gson gson = new Gson();
        JsonFile jsonFile = gson.fromJson(sb.toString(), JsonFile.class);
        List<String> urlImages = new ArrayList<>();
        if (jsonFile != null){
            for(Hit hit: jsonFile.getHits()){
                urlImages.add(hit.getLargeImageURL());
            }
        } else return null;
        return urlImages;
    }
    public List<String> getImage(String searchRequest){
      return getImage(searchRequest,1);
    }
}
