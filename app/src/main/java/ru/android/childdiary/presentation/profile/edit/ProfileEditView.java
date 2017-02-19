package ru.android.childdiary.presentation.profile.edit;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

public interface ProfileEditView extends BaseActivityView {
    void childAdded(Child child);

    void childUpdated(Child child);
}
