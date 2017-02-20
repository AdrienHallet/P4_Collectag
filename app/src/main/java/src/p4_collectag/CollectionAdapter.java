package src.p4_collectag;

import android.support.v4.content.ContextCompat;
import android.view.ActionMode;
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
    private MainActivity mActivity;
    public ActionMode mActionMode;
    private final List<Category> selectedCategories;
    private final List<BaseItem> selectedItems;
    boolean isMultiSelect = false;

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
        return new CategoryViewHolder(view, this);
    }

    @Override
    public BaseItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_base, parent, false);
        return new BaseItemViewHolder(view, this);
    }

    void deleteSelection() {
        expandableList.groups.removeAll(selectedCategories);
        for(ExpandableGroup group : expandableList.groups) group.getItems().removeAll(selectedItems);
        notifyDataSetChanged();
        clearSelection();
    }

    void toggleSelection(int flatPosition) {
        ExpandableListPosition position = expandableList.getUnflattenedPosition(flatPosition);
        switch (getItemViewType(flatPosition)) {
            case ExpandableListPosition.GROUP:
                Category category = ((Category) expandableList.getExpandableGroup(position));
                if(selectedCategories.contains(category)) selectedCategories.remove(category);
                else selectedCategories.add(category);
                break;
            case ExpandableListPosition.CHILD:
                BaseItem item = ((Category) expandableList.getExpandableGroup(position)).getItems().get(position.childPos);
                if(selectedItems.contains(item)) selectedItems.remove(item);
                else selectedItems.add(item);
                break;
            default:
                throw new IllegalArgumentException("viewType is not valid : "+position);
        }
        notifyItemChanged(flatPosition);
        if (mActionMode != null) {
            mActionMode.setTitle("Items selected : " + getSelectedCount());
        }
    }

    void clearSelection() {
        selectedCategories.clear();
        selectedItems.clear();
        notifyDataSetChanged();
    }

    int getSelectedCount() {
        return selectedCategories.size() + selectedItems.size();
    }

    @Override
    public void onBindChildViewHolder(BaseItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        BaseItem item = ((Category) group).getItems().get(childIndex);
        holder.mTextView.setText(item.getDisplayText());
        holder.mImageView.setImageResource(item.getDisplayImage());
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


    void filter(String query) {
        final String lowerCaseQuery = query.toLowerCase();
        //TODO filtering
    }
    public void endFilter() {
        //TODO filtering
    }

    public void onItemClick(int flatPosition) {
        ExpandableListPosition position = expandableList.getUnflattenedPosition(flatPosition);
        BaseItem item = ((Category) expandableList.getExpandableGroup(position)).getItems().get(position.childPos);
        Toast.makeText(mActivity, "Data : " + item.getDisplayText(), Toast.LENGTH_SHORT).show();
    }

    public void enableSelectionMode() {
        if (!isMultiSelect) {
            clearSelection();
            isMultiSelect = true;
            Toast.makeText(mActivity, "Selection mode enabled", Toast.LENGTH_SHORT).show();

            if (mActionMode == null) {
                mActionMode = mActivity.startActionMode(mActivity.mActionModeCallback);
            }
        }
        if (mActionMode != null) {
            mActionMode.setTitle("Items selected : " + getSelectedCount());
        }
    }

}
