package src.p4_collectag;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

/**
 * @author Julien Amalaberque
 */
class CategoryViewHolder extends ParentViewHolder<Category, BaseItem> {
    private static final int CATEGORY_ICON = R.drawable.ic_menu_gallery;
    private TextView name;
    private ImageView mImageView;
    private FrameLayout mLayout;
    private MainActivity mActivity;

    CategoryViewHolder(View view, MainActivity activity) {
        super(view);
        mActivity = activity;
        name = (TextView) view.findViewById(R.id.item_header_name);
        mImageView = (ImageView) view.findViewById(R.id.item_header_image);
        mLayout = (FrameLayout) view.findViewById(R.id.list_item_category);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                mActivity.enableSelectionMode();
                if (mActivity.selectedCategories.contains(getParent())) {
                    mActivity.selectedCategories.remove(getParent());
                } else {
                    mActivity.selectedCategories.add(getParent());
                }
                mActivity.notifySelectionChanged();
                return true;
            }
        });
    }

    void bind(@NonNull Category group) {
        name.setText(group.getTitle());
        mImageView.setImageResource(CATEGORY_ICON);
        if (mActivity.selectedCategories.contains(group))
            mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_selected_state));
        else
            mLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.list_item_normal_state));
    }
}
