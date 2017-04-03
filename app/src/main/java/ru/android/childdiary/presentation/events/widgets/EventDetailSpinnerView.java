package ru.android.childdiary.presentation.events.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.Setter;
import ru.android.childdiary.R;

public abstract class EventDetailSpinnerView<T> extends LinearLayout implements
        AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener,
        View.OnClickListener {
    @BindView(R.id.textViewWrapper)
    View textViewWrapper;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindDimen(R.dimen.spinner_item_width)
    int spinnerItemWidth;

    @Setter
    private EventDetailSpinnerListener<T> eventDetailSpinnerListener;
    @Getter
    private T value;

    private ListPopupWindow popupWindow;

    public EventDetailSpinnerView(Context context) {
        super(context);
        init();
    }

    public EventDetailSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventDetailSpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), getLayoutResourceId(), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setValue(T value) {
        if (this.value != value) {
            this.value = value;
            textView.setText(getTextForValue(value));
            valueChanged();
        }
    }

    protected void valueChanged() {
    }

    public boolean dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        popupWindow = null;
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == textViewWrapper) {
            dismissPopupWindow();
            View anchor = view;
            int width = spinnerItemWidth;
            int gravity = Gravity.END;

            popupWindow = new android.support.v7.widget.ListPopupWindow(getContext(), null, R.attr.actionOverflowMenuStyle, R.style.OverflowMenu);
            popupWindow.setWidth(width);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAdapter(getAdapter());
            popupWindow.setAnchorView(anchor);
            popupWindow.setDropDownGravity(gravity);
            popupWindow.setOnItemClickListener(this);
            popupWindow.setOnDismissListener(this);
            popupWindow.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopupWindow();
        //noinspection unchecked
        T value = (T) parent.getAdapter().getItem(position);
        if (eventDetailSpinnerListener != null) {
            eventDetailSpinnerListener.onSpinnerItemClick(this, value);
        }
    }

    @Override
    public void onDismiss() {
        dismissPopupWindow();
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract String getTextForValue(@Nullable T value);

    protected abstract ListAdapter getAdapter();

    public interface EventDetailSpinnerListener<T> {
        void onSpinnerItemClick(EventDetailSpinnerView view, T item);
    }
}
