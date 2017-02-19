package src.p4_collectag;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * @author Julien Amalaberque
 */
class ItemCategory implements ViewModel {
    static final ItemCategory ROOT_CATEGORY = null;
    private int displayText;
    private int displayImage;
    private ItemCategory parentCategory;

    public ItemCategory(@StringRes int displayTextIn, @DrawableRes int displayImageIn, ItemCategory parentCategoryIn) {
        displayText = displayTextIn;
        displayImage = displayImageIn;
        parentCategory = parentCategoryIn;
    }

    public ItemCategory(@StringRes int displayTextIn, @DrawableRes int displayImageIn) {
        this(displayTextIn, displayImageIn, ROOT_CATEGORY);
    }

    @Override
    public int getDisplayText() {
        return displayText;
    }

    @Override
    public int getDisplayImage() {
        return displayImage;
    }

    @Override
    public ItemCategory getCategory() {
        return parentCategory;
    }
}
