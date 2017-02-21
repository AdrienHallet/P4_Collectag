package src.p4_collectag;

import android.graphics.Bitmap;
import android.os.Parcelable;

/**
 * An item that can be displayed in CollecTag collections.
 *
 * @author Julien Amalaberque
 */
public interface BaseItem extends Parcelable {
    String getDisplayText();
    Bitmap getDisplayImage();
}

