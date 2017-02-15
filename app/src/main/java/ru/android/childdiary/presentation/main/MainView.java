package ru.android.childdiary.presentation.main;

import java.util.List;

import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

public interface MainView extends BaseActivityView {
    void showChildList(List<Child> childList);

    void showAntropometryList(List<Antropometry> antropometryList);
}
