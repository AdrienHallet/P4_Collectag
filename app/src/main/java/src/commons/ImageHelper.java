package src.commons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Helper class. It allows bitmap and byte arrays handling
 *
 * @author Adrien
 * @version 1.02
 *          Changelog : fixed url conversion error
 */

public class ImageHelper {
    //How many bytes of an image should be processed
    private static final int CHUNK_SIZE = 4096;

    /**
     * Convert remote image into byte array
     *
     * @param url the resource url
     * @return a byte array representing the image
     */
    public static byte[] getImageFromURL(String url) throws BookException {
        URL url_object;
        try {
            url_object = new URL(url);
        } catch (MalformedURLException e) {
            Log.d("Image Helper", "Could not translate given string in URL :\n" + e.toString());
            throw new BookException(BookException.NO_COVER_FOUND);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[CHUNK_SIZE];
            int bytesRead;
            InputStream stream = url_object.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            throw new BookException(BookException.NO_COVER_FOUND);
        }

        return outputStream.toByteArray();
    }

    /**
     * Bitmap image to byte array
     *
     * @param bitmap the image
     * @return a byte array representing the image
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * Byte array to bitmap image
     *
     * @param image the byte array representing an image
     * @return the corresponding Bitmap image
     */
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
