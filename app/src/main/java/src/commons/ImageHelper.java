package src.commons;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;

/**
 * Created by Adrien on 17/02/2017.
 */

public class ImageHelper {

    public static byte[] getImageFromURL(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }
}
