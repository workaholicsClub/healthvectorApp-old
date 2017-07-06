package ru.android.childdiary.presentation.core.bindings;

import android.support.annotation.Nullable;

import com.jakewharton.rxbinding2.InitialValueObservable;

import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import ru.android.childdiary.presentation.core.fields.widgets.FieldValueView;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

public final class FieldValueChangeEventsObservable<T>
        extends InitialValueObservable<FieldValueEvent<T>> {
    private final FieldValueView<T> view;

    public FieldValueChangeEventsObservable(FieldValueView<T> view) {
        this.view = view;
    }

    @Override
    protected void subscribeListener(Observer<? super FieldValueEvent<T>> observer) {
        if (!checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener<>(view, observer);
        //noinspection unchecked
        view.addValueChangeListener(listener);
        observer.onSubscribe(listener);
    }

    @Override
    protected FieldValueEvent<T> getInitialValue() {
        return new FieldValueEvent<>(view, view.getValue());
    }

    private static final class Listener<T> extends MainThreadDisposable implements FieldValueView.ValueChangeListener<T> {
        private final FieldValueView<T> view;
        private final Observer<? super FieldValueEvent<T>> observer;

        public Listener(FieldValueView<T> view, Observer<? super FieldValueEvent<T>> observer) {
            this.observer = observer;
            this.view = view;
        }

        @Override
        public void onValueChange(@Nullable T value) {
            if (!isDisposed()) {
                observer.onNext(new FieldValueEvent<>(view, value));
            }
        }

        @Override
        protected void onDispose() {
            view.removeValueChangeListener(this);
        }
    }
}
