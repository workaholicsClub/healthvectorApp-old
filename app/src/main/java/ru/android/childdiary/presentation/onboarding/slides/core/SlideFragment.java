package ru.android.childdiary.presentation.onboarding.slides.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.BaseMvpFragment;

public abstract class SlideFragment extends BaseMvpFragment {
    @BindView(R.id.textView)
    TextView textView;

    @Nullable
    @Override
    protected Sex getSex() {
        return null;
    }

    @LayoutRes
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_slide;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView.setText(getTextResourceId());

        // TODO onboarding images
    }

    @StringRes
    protected abstract int getTextResourceId();
}