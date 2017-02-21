package src.p4_collectag;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * An item that can be displayed in CollecTag collections.
 *
 * @author Julien Amalaberque
 */
class BaseItem implements Parcelable {
    public static final Creator<BaseItem> CREATOR = new Creator<BaseItem>() {
        @Override
        public BaseItem createFromParcel(Parcel in) {
            return new BaseItem(in);
        }

        @Override
        public BaseItem[] newArray(int size) {
            return new BaseItem[size];
        }
    };
    protected String displayText;
    protected int displayImage;

    BaseItem(String displayTextIn, @DrawableRes int displayImageIn) {
        displayText = displayTextIn;
        displayImage = displayImageIn;
    }

    protected BaseItem(Parcel in) {
        displayText = in.readString();
        displayImage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayText);
        dest.writeInt(displayImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDisplayText() {
        return displayText;
    }

    public int getDisplayImage() {
        return displayImage;
    }
}

