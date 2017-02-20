package src.p4_collectag;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * An item that can be displayed in CollecTag collections.
 *
 * @author Julien Amalaberque
 */
abstract class BaseItem implements Parcelable {
    private String displayText;
    private int displayImage;

    BaseItem(String displayTextIn, @DrawableRes int displayImageIn) {
        displayText = displayTextIn;
        displayImage = displayImageIn;
    }

    public String getDisplayText() {
        return displayText;
    }

    public int getDisplayImage() {
        return displayImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

