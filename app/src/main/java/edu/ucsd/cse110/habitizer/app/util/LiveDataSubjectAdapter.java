package edu.ucsd.cse110.habitizer.app.util;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.observables.Observer;
import edu.ucsd.cse110.observables.Subject;

public class LiveDataSubjectAdapter<T> implements Subject<T> {
    private final LiveData<T> adaptee;
    private final List<Observer<T>> observers = new ArrayList<>();
    private final Map<Observer<T>, androidx.lifecycle.Observer<T>> observerMap = new HashMap<>();
    public LiveDataSubjectAdapter(LiveData<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Nullable
    @Override
    public T getValue() {
        return adaptee.getValue();
    }

    @Override
    public boolean hasObservers() {
        return !observers.isEmpty();
    }

    @Override
    public boolean isInitialized() {
        return adaptee.getValue() != null;
    }

    @Override
    public Observer<T> observe(Observer<T> observer) {
        androidx.lifecycle.Observer<T> liveDataObserver = observer::onChanged;
        observerMap.put(observer, liveDataObserver);
        observers.add(observer);
        adaptee.observeForever(liveDataObserver);
        return observer;
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        androidx.lifecycle.Observer<T> liveDataObserver = observerMap.get(observer);
        if (liveDataObserver != null) {
            adaptee.removeObserver(liveDataObserver);
            observerMap.remove(observer);
            observers.remove(observer);
        }
    }

    @Override
    public void removeObservers() {
        for (Map.Entry<Observer<T>, androidx.lifecycle.Observer<T>> entry : observerMap.entrySet()) {
            adaptee.removeObserver(entry.getValue());
        }
        observerMap.clear();
        observers.clear();
    }

    @Override
    public List<Observer<T>> getObservers() {
        return new ArrayList<>(observers);
    }

    @Override
    public void setValue(T newValue){

    }
}
