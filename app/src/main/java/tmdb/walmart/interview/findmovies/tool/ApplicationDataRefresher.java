package tmdb.walmart.interview.findmovies.tool;

import android.content.Context;

import java.io.File;

/**
 * Created by Yee on 5/22/17.
 */

public class ApplicationDataRefresher {

    private Context mContext;

    public ApplicationDataRefresher(Context context) {
        mContext = context;
    }

    public void clearApplicationData() {
        File cache = mContext.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null && dir.delete();
    }
}
