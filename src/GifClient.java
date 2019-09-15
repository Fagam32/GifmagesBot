import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;

import java.util.ArrayList;
import java.util.List;

class GifClient {

    private List<String> getGif(String searchRequest, int offset){
        List<String> gifs = new ArrayList<>();
        try {
            Giphy giphy = new Giphy("");
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
    List<String> getGif(String searchRequest){
        return getGif(searchRequest,0);
    }
}
