package src.p4_collectag;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * An item that can be displayed in CollecTag collections.
 */
public interface ListItem {
    @StringRes
    int getDisplayText();

    @DrawableRes
    int getDisplayImage();

    /**
     * Called when the item was clicked
     *
     * @param view The view linked to the item
     */
    void onClick(View view);
}
