package ru.android.childdiary.presentation.help;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class HelpFragment extends AppPartitionFragment implements HelpView {
    @InjectPresenter
    HelpPresenter presenter;

    @BindView(R.id.rootView)
    View rootView;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_help;
    }

    @Override
    protected void setupUi() {
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        rootView.setBackgroundResource(ThemeUtils.getColorPrimaryRes(getSex()));
    }
}
