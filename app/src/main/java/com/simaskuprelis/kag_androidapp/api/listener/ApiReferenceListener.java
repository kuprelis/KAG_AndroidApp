package com.simaskuprelis.kag_androidapp.api.listener;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.WorkerThread;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public abstract class ApiReferenceListener<T, L> implements ValueEventListener {

    private final Class<T> mClass;
    private final WeakReference<L> mListenerReference;

    protected ApiReferenceListener(Class<T> cls, WeakReference<L> weakRef) {
        mClass = cls;
        mListenerReference = weakRef;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (mListenerReference.get() != null) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    final Map<String, T> items = new HashMap<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        T item = snapshot.getValue(mClass);
                        String key = snapshot.getKey();
                        if (where(item)) items.put(key, item);
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            L listener = mListenerReference.get();
                            if (listener != null) onLoaded(items, listener);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        handleException(databaseError.toException());
    }

    private void handleException(Exception ex) {
        // TODO crash reporting
        //FirebaseCrash.report(ex);

        L listener = mListenerReference.get();
        if (listener != null) {
            onFailed(listener, ex);
        }
    }

    @WorkerThread
    public boolean where(T item) {
        return true;
    }

    public abstract void onFailed(L listener, Exception ex);

    public abstract void onLoaded(Map<String, T> items, L listener);
}