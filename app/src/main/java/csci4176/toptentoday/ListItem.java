package csci4176.toptentoday;

import org.json.JSONObject;

/**
 * Created by Blair on 3/13/2016.
 */
public class ListItem {
    public String title;
    public String subTitle;
    public String imgUrl;
    public String url;
    public JSONObject json;

    public ListItem(String title, String subTitle, String imgUrl, String url, JSONObject json) {
        this.title = title;
        this.subTitle = subTitle;
        this.imgUrl = imgUrl;
        this.url = url;
        this.json = json;
    }
}
