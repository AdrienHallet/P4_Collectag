package src.p4_collectag;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * @author Julien Amalaberque
 */
class CategoryViewHolder extends GroupViewHolder {
    TextView name;
    ImageView mImageView;
    FrameLayout mLayout;
    CollectionAdapter mAdapter;

    CategoryViewHolder(View view, CollectionAdapter adapter) {
        super(view);
        mAdapter = adapter;
        name = (TextView) view.findViewById(R.id.item_header_name);
        mImageView = (ImageView) view.findViewById(R.id.item_header_image);
        mLayout = (FrameLayout) view.findViewById(R.id.list_item_category);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CategoryViewHolder.this.handleClick();
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                CategoryViewHolder.this.handleLongClick();
                return true;
            }
        });
    }

    protected void handleClick() {
        int flatPosition = getLayoutPosition();
        if (mAdapter.isMultiSelect) {
            mAdapter.toggleSelection(flatPosition);
        } else {
            mAdapter.toggleGroup(flatPosition);
        }
    }

    protected void handleLongClick() {
        mAdapter.enableSelectionMode();
    }

}
