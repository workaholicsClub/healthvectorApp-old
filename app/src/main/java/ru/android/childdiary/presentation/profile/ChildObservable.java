package ru.android.childdiary.presentation.profile;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import ru.android.childdiary.domain.child.data.Child;

class ChildObservable extends Observable<Child> {
    private final ProfileEditActivity activity;

    public ChildObservable(ProfileEditActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void subscribeActual(Observer<? super Child> observer) {
        Listener listener = new Listener(activity, observer);
        listener.subscribe();
    }

    private static class Listener extends MainThreadDisposable implements OnUpdateChildListener {
        private final ProfileEditActivity activity;
        private final Observer<? super Child> observer;

        public Listener(ProfileEditActivity activity, Observer<? super Child> observer) {
            this.activity = activity;
            this.observer = observer;
        }

        public void subscribe() {
            activity.addOnUpdateChildListener(this);
            observer.onSubscribe(this);
            observer.onNext(activity.editedChild);
        }

        @Override
        public void onUpdateChild(@NonNull Child child) {
            observer.onNext(child);
        }

        @Override
        protected void onDispose() {
            activity.removeOnUpdateChildListener(this);
        }
    }
}
