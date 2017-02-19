package src.p4_collectag;

/**
 * @author Julien Amalaberque
 */
class ExampleModel implements ViewModel {
    @Override
    public int getDisplayText() {
        return R.string.default_item_name;
    }

    @Override
    public int getDisplayImage() {
        return R.drawable.ic_menu_gallery;
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.ROOT_CATEGORY;
    }
}
