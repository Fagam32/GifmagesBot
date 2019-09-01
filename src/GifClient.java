import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;

import java.util.ArrayList;
import java.util.List;

public class GifClient {

    public List<String> getGif(String searchRequest, int offset){
        List<String> gifs = new ArrayList<>();
        try {
            Giphy giphy = new Giphy("4caDwU7SveMqV06e6NuALne91yDS6UUn");
            SearchFeed searchFeed = giphy.search(searchRequest, 20, offset);
            for (int i = 0; i < searchFeed.getDataList().size(); i++) {
                gifs.add(searchFeed.getDataList().get(i).getImages().getOriginal().getUrl());
            }
        } catch (GiphyException e){
            e.printStackTrace();
            return null;
        }
        return gifs;
    }
    public List<String> getGif(String searchRequest){
        return getGif(searchRequest,0);
    }
}
