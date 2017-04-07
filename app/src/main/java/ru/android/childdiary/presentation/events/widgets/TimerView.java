package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class TimerView extends RelativeLayout {
    @BindView(R.id.buttonTimer)
    View buttonTimer;

    @BindView(R.id.imageViewTimer)
    ImageView imageViewTimer;

    @BindView(R.id.textViewLabel)
    TextView textViewLabel;

    @BindView(R.id.textViewTimer)
    TextView textViewTimer;

    private boolean isOn;
    private Sex sex;

    public TimerView(Context context) {
        super(context);
        init();
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.timer_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        textViewLabel.setTypeface(FontUtils.getTypefaceMedium(getContext()));
    }

    public void setOn(boolean isOn) {
        if (this.isOn != isOn) {
            this.isOn = isOn;
            setupUi(getContext());
        }
    }

    public void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            setupUi(getContext());
        }
    }

    public void setText(String text) {
        textViewTimer.setText(text);
    }

    private void setupUi(Context context) {
        textViewLabel.setText(isOn ? R.string.stop_timer : R.string.start_timer);
        textViewLabel.setTextColor(ResourcesUtils.getTimerTextColor(context, sex, isOn));
        textViewTimer.setTextColor(ResourcesUtils.getTimerTextColor(context, sex, isOn));
        buttonTimer.setBackgroundResource(ResourcesUtils.getTimerBackgroundRes(sex, isOn));
        imageViewTimer.setImageResource(ResourcesUtils.getTimerIcon(sex, isOn));
    }
}
