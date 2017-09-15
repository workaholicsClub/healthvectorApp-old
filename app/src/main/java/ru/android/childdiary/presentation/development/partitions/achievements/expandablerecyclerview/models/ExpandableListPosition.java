package ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models;

import android.widget.ExpandableListView;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Exact copy of android.widget.ExpandableListPosition because
 * android.widget.ExpandableListPosition has package local scope.
 * <p>
 * ExpandableListPosition can refer to either a group's position or a child's
 * position. Referring to a child's position requires both a group position (the
 * group containing the child) and a child position (the child's position within
 * that group). To create objects, use {@link #obtainChildPosition(int, int)} or
 * {@link #obtainGroupPosition(int)}.
 */
@ToString
@EqualsAndHashCode
public class ExpandableListPosition {
    public static final int FOOTER = -1;
    
    /**
     * This data type represents a child position
     */
    public static final int CHILD = 1;

    /**
     * This data type represents a group position
     */
    public static final int GROUP = 2;

    private static final int MAX_POOL_SIZE = 5;

    private static final ArrayList<ExpandableListPosition> POOL = new ArrayList<>(MAX_POOL_SIZE);

    /**
     * The position of either the group being referred to, or the parent
     * group of the child being referred to
     */
    public int groupPos;

    /**
     * The position of the child within its parent group
     */
    public int childPos;

    /**
     * What type of position this ExpandableListPosition represents
     */
    public int type;

    /**
     * The position of the item in the flat list (optional, used internally when
     * the corresponding flat list position for the group or child is known)
     */
    int flatListPos;

    private ExpandableListPosition() {
    }

    static ExpandableListPosition obtainGroupPosition(int groupPosition) {
        return obtain(GROUP, groupPosition, 0, 0);
    }

    static ExpandableListPosition obtainChildPosition(int groupPosition, int childPosition) {
        return obtain(CHILD, groupPosition, childPosition, 0);
    }

    static ExpandableListPosition obtainPosition(long packedPosition) {
        if (packedPosition == ExpandableListView.PACKED_POSITION_VALUE_NULL) {
            return null;
        }

        ExpandableListPosition elp = getRecycledOrCreate();
        elp.groupPos = ExpandableListView.getPackedPositionGroup(packedPosition);
        if (ExpandableListView.getPackedPositionType(packedPosition) ==
                ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            elp.type = CHILD;
            elp.childPos = ExpandableListView.getPackedPositionChild(packedPosition);
        } else {
            elp.type = GROUP;
        }
        return elp;
    }

    public static ExpandableListPosition obtain(int type, int groupPos, int childPos,
                                                int flatListPos) {
        ExpandableListPosition elp = getRecycledOrCreate();
        elp.type = type;
        elp.groupPos = groupPos;
        elp.childPos = childPos;
        elp.flatListPos = flatListPos;
        return elp;
    }

    private static ExpandableListPosition getRecycledOrCreate() {
        ExpandableListPosition elp;
        synchronized (POOL) {
            if (POOL.size() > 0) {
                elp = POOL.remove(0);
            } else {
                return new ExpandableListPosition();
            }
        }
        elp.resetState();
        return elp;
    }

    private void resetState() {
        groupPos = 0;
        childPos = 0;
        flatListPos = 0;
        type = 0;
    }

    public long getPackedPosition() {
        if (type == CHILD) {
            return ExpandableListView.getPackedPositionForChild(groupPos, childPos);
        } else {
            return ExpandableListView.getPackedPositionForGroup(groupPos);
        }
    }

    /**
     * Do not call this unless you obtained this via ExpandableListPosition.obtain().
     * PositionMetadata will handle recycling its own children.
     */
    public void recycle() {
        synchronized (POOL) {
            if (POOL.size() < MAX_POOL_SIZE) {
                POOL.add(this);
            }
        }
    }
}
