package tmdb.walmart.interview.findmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yee on 5/20/17.
 */

public interface ParseData {
    void parseData(JSONObject object) throws JSONException;
}
