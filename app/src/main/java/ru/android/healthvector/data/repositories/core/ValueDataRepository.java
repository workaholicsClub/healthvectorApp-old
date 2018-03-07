package ru.android.healthvector.data.repositories.core;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.android.healthvector.domain.core.ValueRepository;

public abstract class ValueDataRepository<T> implements ValueRepository<T> {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private final List<OnSelectedValueChangedListener<T>> selectedValueChangedListeners = new ArrayList<>();

    private T selectedValue = getDefaultValue();

    public ValueDataRepository() {
    }

    void addOnActiveChildChangedListener(OnSelectedValueChangedListener<T> listener) {
        synchronized (selectedValueChangedListeners) {
            selectedValueChangedListeners.add(listener);
        }
    }

    void removeOnActiveChildChangedListener(OnSelectedValueChangedListener<T> listener) {
        synchronized (selectedValueChangedListeners) {
            selectedValueChangedListeners.remove(listener);
        }
    }

    protected abstract T getDefaultValue();

    @Override
    public Observable<T> getSelectedValue() {
        return new SelectedValueObservable();
    }

    @Override
    public void setSelectedValue(@NonNull T value) {
        selectedValue = value;
        logger.debug("setSelectedValue: " + value);
        synchronized (selectedValueChangedListeners) {
            for (OnSelectedValueChangedListener<T> listener : selectedValueChangedListeners) {
                listener.onSelectedValueChanged(value);
            }
        }
    }

    @Override
    public Observable<T> getSelectedValueOnce() {
        return getSelectedValue()
                .first(getDefaultValue())
                .toObservable();
    }

    @Override
    public Observable<T> setSelectedValueObservable(@NonNull T value) {
        return Observable.fromCallable(() -> {
            setSelectedValue(value);
            return value;
        });
    }

    private interface OnSelectedValueChangedListener<T> {
        void onSelectedValueChanged(T value);
    }

    private class SelectedValueObservable extends Observable<T> {
        @Override
        protected void subscribeActual(Observer<? super T> observer) {
            OnSelectedValueChangedSubscription listener = new OnSelectedValueChangedSubscription(observer);
            listener.subscribe();
        }
    }

    private class OnSelectedValueChangedSubscription implements Disposable, OnSelectedValueChangedListener<T> {
        private final Observer<? super T> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        public OnSelectedValueChangedSubscription(Observer<? super T> observer) {
            this.observer = observer;
        }

        public void subscribe() {
            addOnActiveChildChangedListener(this);
            observer.onSubscribe(this);
            observer.onNext(selectedValue);
        }

        @Override
        public void onSelectedValueChanged(@NonNull T value) {
            observer.onNext(value);
        }

        @Override
        public void dispose() {
            if (unsubscribed.compareAndSet(false, true)) {
                removeOnActiveChildChangedListener(this);
            }
        }

        @Override
        public boolean isDisposed() {
            return unsubscribed.get();
        }
    }
}
