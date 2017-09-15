package ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.listeners.ExpandCollapseListener;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.listeners.OnGroupClickListener;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models.ExpandableGroup;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models.ExpandableList;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models.ExpandableListPosition;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.viewholders.ChildViewHolder;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.viewholders.GroupViewHolder;

public abstract class ExpandableRecyclerViewAdapter<GVH extends GroupViewHolder, CVH extends ChildViewHolder>
        extends RecyclerView.Adapter implements ExpandCollapseListener, OnGroupClickListener {
    private static final String KEY_EXPAND_STATE_MAP = "key_expand_state_map";

    protected final Context context;
    protected final LayoutInflater inflater;
    private final ExpandableList expandableList;
    private final ExpandCollapseController expandCollapseController;

    @Setter
    private OnGroupClickListener onGroupClickListener;
    @Setter
    private GroupExpandCollapseListener groupExpandCollapseListener;

    public ExpandableRecyclerViewAdapter(Context context, List<? extends ExpandableGroup> groups) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.expandableList = new ExpandableList(groups);
        this.expandCollapseController = new ExpandCollapseController(expandableList, this);
    }

    /**
     * Implementation of Adapter.onCreateViewHolder(ViewGroup, int)
     * that determines if the list item is a group or a child and calls through
     * to the appropriate implementation of either {@link #onCreateGroupViewHolder(ViewGroup, int)}
     * or {@link #onCreateChildViewHolder(ViewGroup, int)}}.
     *
     * @param parent   The {@link ViewGroup} into which the new {@link android.view.View}
     *                 will be added after it is bound to an adapter position.
     * @param viewType The view type of the new {@code android.view.View}.
     * @return Either a new {@link GroupViewHolder} or a new {@link ChildViewHolder}
     * that holds a {@code android.view.View} of the given view type.
     */
    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ExpandableListPosition.GROUP:
                GVH gvh = onCreateGroupViewHolder(parent, viewType);
                gvh.setOnGroupClickListener(this);
                return gvh;
            case ExpandableListPosition.CHILD:
                CVH cvh = onCreateChildViewHolder(parent, viewType);
                return cvh;
            case ExpandableListPosition.FOOTER:
                View v = inflater.inflate(R.layout.view_footer, parent, false);
                return new FooterViewHolder(v);
            default:
                throw new IllegalArgumentException("viewType is not valid");
        }
    }

    /**
     * Implementation of Adapter.onBindViewHolder(RecyclerView.ViewHolder, int)
     * that determines if the list item is a group or a child and calls through
     * to the appropriate implementation of either {@link #onBindGroupViewHolder(GroupViewHolder,
     * int, ExpandableGroup)}
     * or {@link #onBindChildViewHolder(ChildViewHolder, int, ExpandableGroup, int)}.
     *
     * @param holder   Either the GroupViewHolder or the ChildViewHolder to bind data to
     * @param position The flat position (or index in the list of {@link
     *                 ExpandableList#getVisibleItemCount()} in the list at which to bind
     */
    @Override
    @SuppressWarnings("unchecked")
    public final void onBindViewHolder(ViewHolder holder, int position) {
        if (position >= expandableList.getVisibleItemCount()) {
            // it's footer
            return;
        }
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(position);
        ExpandableGroup group = expandableList.getExpandableGroup(listPos);
        switch (listPos.type) {
            case ExpandableListPosition.GROUP:
                onBindGroupViewHolder((GVH) holder, position, group);
                if (isGroupExpanded(group)) {
                    ((GVH) holder).expand();
                } else {
                    ((GVH) holder).collapse();
                }
                break;
            case ExpandableListPosition.CHILD:
                onBindChildViewHolder((CVH) holder, position, group, listPos.childPos);
                break;
        }
    }

    /**
     * @return the number of group and child objects currently expanded
     * @see ExpandableList#getVisibleItemCount()
     */
    @Override
    public int getItemCount() {
        return expandableList.getVisibleItemCount() + 1;
    }

    /**
     * Gets the view type of the item at the given position.
     *
     * @param position The flat position in the list to get the view type of
     * @return {@value ExpandableListPosition#CHILD} or {@value ExpandableListPosition#GROUP}
     * @throws RuntimeException if the item at the given position in the list is not found
     */
    @Override
    public int getItemViewType(int position) {
        return position >= expandableList.getVisibleItemCount()
                ? ExpandableListPosition.FOOTER
                : expandableList.getUnflattenedPosition(position).type;
    }

    /**
     * Called when a group is expanded
     *
     * @param positionStart the flat position of the first child in the {@link ExpandableGroup}
     * @param itemCount     the total number of children in the {@link ExpandableGroup}
     */
    @Override
    public void onGroupExpanded(int positionStart, int itemCount) {
        // update header
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        // only insert if there items to insert
        if (itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
            if (groupExpandCollapseListener != null) {
                int groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos;
                groupExpandCollapseListener.onGroupExpanded(getGroups().get(groupIndex));
            }
        }
    }

    /**
     * Called when a group is collapsed
     *
     * @param positionStart the flat position of the first child in the {@link ExpandableGroup}
     * @param itemCount     the total number of children in the {@link ExpandableGroup}
     */
    @Override
    public void onGroupCollapsed(int positionStart, int itemCount) {
        // update header
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        // only remove if there items to remove
        if (itemCount > 0) {
            notifyItemRangeRemoved(positionStart, itemCount);
            if (groupExpandCollapseListener != null) {
                // minus one to return the position of the header, not first child
                int groupIndex = expandableList.getUnflattenedPosition(positionStart - 1).groupPos;
                groupExpandCollapseListener.onGroupCollapsed(getGroups().get(groupIndex));
            }
        }
    }

    /**
     * Triggered by a click on a {@link GroupViewHolder}
     *
     * @param flatPos the flat position of the {@link GroupViewHolder} that was clicked
     * @return false if click expanded group, true if click collapsed group
     */
    @Override
    public boolean onGroupClick(int flatPos) {
        if (onGroupClickListener != null) {
            onGroupClickListener.onGroupClick(flatPos);
        }
        return expandCollapseController.toggleGroup(flatPos);
    }

    /**
     * @param flatPos The flat list position of the group
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    public boolean toggleGroup(int flatPos) {
        return expandCollapseController.toggleGroup(flatPos);
    }

    /**
     * @param group the {@link ExpandableGroup} being toggled
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    public boolean toggleGroup(ExpandableGroup group) {
        return expandCollapseController.toggleGroup(group);
    }

    /**
     * @param flatPos the flattened position of an item in the list
     * @return true if {@code group} is expanded, false if it is collapsed
     */
    public boolean isGroupExpanded(int flatPos) {
        return expandCollapseController.isGroupExpanded(flatPos);
    }

    /**
     * @param group the {@link ExpandableGroup} being checked for its collapsed state
     * @return true if {@code group} is expanded, false if it is collapsed
     */
    public boolean isGroupExpanded(ExpandableGroup group) {
        return expandCollapseController.isGroupExpanded(group);
    }

    /**
     * Stores the expanded state map across state loss.
     * <p>
     * Should be called from whatever {@link Activity} that hosts the RecyclerView that {@link
     * ExpandableRecyclerViewAdapter} is attached to.
     * <p>
     * This will make sure to add the expanded state map as an extra to the
     * instance state bundle to be used in {@link #onRestoreInstanceState(Bundle)}.
     *
     * @param savedInstanceState The {@code Bundle} into which to store the
     *                           expanded state map
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // TODO useless?
        savedInstanceState.putBooleanArray(KEY_EXPAND_STATE_MAP, expandableList.expandedGroupIndexes);
    }

    /**
     * Fetches the expandable state map from the saved instance state {@link Bundle}
     * and restores the expanded states of all of the list items.
     * <p>
     * Should be called from {@link Activity#onRestoreInstanceState(Bundle)}  in
     * the {@link Activity} that hosts the RecyclerView that this
     * {@link ExpandableRecyclerViewAdapter} is attached to.
     *
     * @param savedInstanceState The {@code Bundle} from which the expanded
     *                           state map is loaded
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_EXPAND_STATE_MAP)) {
            return;
        }
        expandableList.expandedGroupIndexes = savedInstanceState.getBooleanArray(KEY_EXPAND_STATE_MAP);
        notifyDataSetChanged();
    }

    /**
     * The full list of {@link ExpandableGroup} backing this RecyclerView
     *
     * @return the list of {@link ExpandableGroup} that this object was instantiated with
     */
    public List<? extends ExpandableGroup> getGroups() {
        return expandableList.getGroups();
    }

    /**
     * Called from {@link #onCreateViewHolder(ViewGroup, int)} when  the list item created is a group
     *
     * @param viewType an int returned by {@link ExpandableRecyclerViewAdapter#getItemViewType(int)}
     * @param parent   the {@link ViewGroup} in the list for which a {@link GVH}  is being created
     * @return A {@link GVH} corresponding to the group list item with the  {@code ViewGroup} parent
     */
    public abstract GVH onCreateGroupViewHolder(ViewGroup parent, int viewType);

    /**
     * Called from {@link #onCreateViewHolder(ViewGroup, int)} when the list item created is a child
     *
     * @param viewType an int returned by {@link ExpandableRecyclerViewAdapter#getItemViewType(int)}
     * @param parent   the {@link ViewGroup} in the list for which a {@link CVH}  is being created
     * @return A {@link CVH} corresponding to child list item with the  {@code ViewGroup} parent
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item
     * bound to is a  child.
     * <p>
     * Bind data to the {@link CVH} here.
     *
     * @param holder       The {@code CVH} to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the child
     * @param group        The {@link ExpandableGroup} that the the child list item belongs to
     * @param childIndex   the index of this child within it's {@link ExpandableGroup}
     */
    public abstract void onBindChildViewHolder(CVH holder, int flatPosition, ExpandableGroup group,
                                               int childIndex);

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item bound to is a
     * group
     * <p>
     * Bind data to the {@link GVH} here.
     *
     * @param holder       The {@code GVH} to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the group
     * @param group        The {@link ExpandableGroup} to be used to bind data to this {@link GVH}
     */
    public abstract void onBindGroupViewHolder(GVH holder, int flatPosition, ExpandableGroup group);

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
