package ru.android.healthvector.presentation.onboarding.slides.core;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseMvpFragment;

public abstract class SlideFragment extends BaseMvpFragment {
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

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
        imageView.setImageResource(getImageResourceId());
    }

    @StringRes
    protected abstract int getTextResourceId();

    @DrawableRes
    protected abstract int getImageResourceId();
}
