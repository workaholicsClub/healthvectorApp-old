package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.FeedType;
import ru.android.childdiary.presentation.events.adapters.FeedTypeAdapter;
import ru.android.childdiary.utils.StringUtils;

public class EventDetailFeedTypeView extends LinearLayout implements
        AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener {
    private final FeedTypeAdapter adapter = new FeedTypeAdapter(getContext());

    @BindView(R.id.textView)
    TextView textView;

    @BindDimen(R.dimen.event_detail_text_width)
    int spinnerItemWidth;

    @Setter
    private OnFeedTypeChanged onFeedTypeChanged;
    @Getter
    private FeedType feedType;

    private ListPopupWindow popupWindow;

    public EventDetailFeedTypeView(Context context) {
        super(context);
        init();
    }

    public EventDetailFeedTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailFeedTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_detail_feed_type, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setFeedType(FeedType value) {
        if (feedType != value) {
            feedType = value;
            textView.setText(StringUtils.feedType(getContext(), value));
            if (onFeedTypeChanged != null) {
                onFeedTypeChanged.onFeedTypeChanged();
            }
        }
    }

    public boolean dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        popupWindow = null;
        return false;
    }

    @OnClick(R.id.textView)
    void onTextViewClick() {
        dismissPopupWindow();
        View anchor = textView;
        int width = spinnerItemWidth;
        int gravity = Gravity.END;

        popupWindow = new ListPopupWindow(getContext(), null, R.attr.actionOverflowMenuStyle, R.style.OverflowMenu);
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAdapter(adapter);
        popupWindow.setAnchorView(anchor);
        popupWindow.setDropDownGravity(gravity);
        popupWindow.setOnItemClickListener(this);
        popupWindow.setOnDismissListener(this);
        popupWindow.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopupWindow();
        FeedType feedType = adapter.getItem(position);
        setFeedType(feedType);
    }

    @Override
    public void onDismiss() {
        dismissPopupWindow();
    }

    public interface OnFeedTypeChanged {
        void onFeedTypeChanged();
    }
}
