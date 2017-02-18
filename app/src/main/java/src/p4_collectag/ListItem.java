package src.p4_collectag;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * An item that can be displayed in CollecTag collections.
 */
public interface ListItem {
    @StringRes
    int getDisplayText();

    @DrawableRes
    int getDisplayImage();
}
