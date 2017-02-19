package src.p4_collectag;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * An item that can be displayed in CollecTag collections.
 *
 * @author Julien Amalaberque
 */
public interface ViewModel {
    @StringRes
    int getDisplayText();

    @DrawableRes
    int getDisplayImage();
}
