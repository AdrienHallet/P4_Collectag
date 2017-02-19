package src.p4_collectag;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represent the displayed list.
 *
 * @author Julien Amalaberque
 */
class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private final Set<ViewModel> selectedItems = new HashSet<>();
    private Context context;
    private Comparator<ViewModel> mComparator;//TODO this could be changed to sort differently
    private final SortedList<ViewModel> displayedList = new SortedList<>(ViewModel.class, new SortedList.Callback<ViewModel>() {
        @Override
        public int compare(ViewModel a, ViewModel b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ViewModel oldItem, ViewModel newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(ViewModel item1, ViewModel item2) {
            return item1.equals(item2);
        }
    });

    CollectionAdapter(Context contextIn, Comparator<ViewModel> mComparatorIn) {
        context = contextIn;
        mComparator = mComparatorIn;
    }

    public ViewModel get(int index) {
        return displayedList.get(index);
    }

    public void add(ViewModel model) {
        displayedList.add(model);
    }

    public void addAll(List<ViewModel> models) {
        displayedList.addAll(models);
    }

    public void removeAll(List<ViewModel> models) {
        displayedList.beginBatchedUpdates();
        for (ViewModel model : models) {
            displayedList.remove(model);
        }
        displayedList.endBatchedUpdates();
    }

    public void replaceAll(List<ViewModel> models) {
        displayedList.beginBatchedUpdates();
        for (int i = displayedList.size() - 1; i >= 0; i--) {
            final ViewModel model = displayedList.get(i);
            if (!models.contains(model)) {
                displayedList.remove(model);
            }
        }
        displayedList.addAll(models);
        displayedList.endBatchedUpdates();
    }

    List<ViewModel> deleteSelection() {
        List<ViewModel> deletedItems = new ArrayList<>();
        for (int i = 0; i < displayedList.size(); i++) {
            ViewModel listItem = displayedList.get(i);
            if (selectedItems.contains(listItem)) {
                deletedItems.add(listItem);
            }
        }
        removeAll(deletedItems);
        clearSelection();
        return deletedItems;
    }

    void toggleSelection(int position) {
        ViewModel item = displayedList.get(position);
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        notifyItemChanged(position);
    }

    void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    int getSelectedCount() {
        return selectedItems.size();
    }

    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // set the data in items
        holder.mTextView.setText(displayedList.get(position).getDisplayText());
        holder.mImageView.setImageResource(displayedList.get(position).getDisplayImage());

        ViewModel item = displayedList.get(position);
        if (selectedItems.contains(item))
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        else
            holder.mLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_normal_state));
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    void filter(List<ViewModel> allItems, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<ViewModel> filteredModelList = new ArrayList<>();
        for (int i = 0; i < allItems.size(); i++) {
            final ViewModel model = allItems.get(i);
            final String text = context.getResources().getString(model.getDisplayText()).toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        replaceAll(filteredModelList);
        //mBinding.recyclerView.scrollToPosition(0);
    }

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        LinearLayout mLayout;

        ViewHolder(View viewIn) {
            super(viewIn);
            mTextView = (TextView) viewIn.findViewById(R.id.name);
            mImageView = (ImageView) viewIn.findViewById(R.id.image);
            mLayout = (LinearLayout) viewIn.findViewById(R.id.item_row);
        }
    }
}
