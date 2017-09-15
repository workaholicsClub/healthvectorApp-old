package ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * The backing data object for an {@link ExpandableGroup}
 */
@Getter
@ToString
public class ExpandableGroup<T> implements Serializable {
    private final String title;
    private final List<T> items;

    public ExpandableGroup(String title, List<T> items) {
        this.title = title;
        this.items = items;
    }

    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
