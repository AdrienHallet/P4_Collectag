package src.database.object;

/**
 * Represent a category
 *
 * @author Adrien
 * @version 1.00
 */

public class CategoryItem {

    private long rowId;
    private String title;

    public CategoryItem() {
        this.rowId = 0;
    }

    public CategoryItem(String title) {
        this.title = title;
        this.rowId = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long id) {
        this.rowId = id;
    }
}
