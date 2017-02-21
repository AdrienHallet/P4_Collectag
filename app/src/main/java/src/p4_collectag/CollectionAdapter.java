package src.p4_collectag;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.List;

/**
 * Represent the displayed list.
 *
 * @author Julien Amalaberque
 */
public class CollectionAdapter extends ExpandableRecyclerViewAdapter<CategoryViewHolder, BaseItemViewHolder> {
    private final List<Category> selectedCategories;
    private final List<BaseItem> selectedItems;
    public MainActivity mActivity;

    CollectionAdapter(MainActivity contextIn, List<? extends ExpandableGroup> groups, List<Category> selectedCategories, List<BaseItem> selectedItems) {
        super(groups);
        mActivity = contextIn;
        this.selectedCategories = selectedCategories;
        this.selectedItems = selectedItems;
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);
        return new CategoryViewHolder(view, mActivity);
    }

    @Override
    public BaseItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_base, parent, false);
        return new BaseItemViewHolder(view, mActivity);
    }


    void toggleSelection(int flatPosition) {
        ExpandableListPosition position = expandableList.getUnflattenedPosition(flatPosition);
        switch (getItemViewType(flatPosition)) {
            case ExpandableListPosition.GROUP:
                Category category = ((Category) expandableList.getExpandableGroup(position));
                if (selectedCategories.contains(category)) selectedCategories.remove(category);
                else selectedCategories.add(category);
                break;
            case ExpandableListPosition.CHILD:
                BaseItem item = ((Category) expandableList.getExpandableGroup(position)).getItems().get(position.childPos);
                if (selectedItems.contains(item)) selectedItems.remove(item);
                else selectedItems.add(item);
                break;
            default:
                throw new IllegalArgumentException("viewType is not valid : " + position);
        }
        notifyItemChanged(flatPosition);
        mActivity.notifySelectionChanged();
    }

    @Override
    public void onBindChildViewHolder(BaseItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        BaseItem item = ((Category) group).getItems().get(childIndex);
        holder.mTextView.setText(item.getDisplayText());
        if(item.getDisplayImage() != null) {
            holder.mImageView.setImageBitmap(item.getDisplayImage());
        }
        else {
            holder.mImageView.setImageResource(R.drawable.ic_menu_slideshow);
        }
        if (selectedItems.contains(item))
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_selected_state));
        else
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_normal_state));
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        Category item = (Category) group;
        holder.name.setText(item.getTitle());
        holder.mImageView.setImageResource(R.drawable.ic_menu_gallery);//TODO category icon
        if (selectedCategories.contains(item))
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_selected_state));
        else
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_normal_state));
    }

    public void onItemClick(int flatPosition) {
        ExpandableListPosition position = expandableList.getUnflattenedPosition(flatPosition);
        BaseItem item = ((Category) expandableList.getExpandableGroup(position)).getItems().get(position.childPos);
        Toast.makeText(mActivity, "Data : " + item.getDisplayText(), Toast.LENGTH_SHORT).show();
    }

}
