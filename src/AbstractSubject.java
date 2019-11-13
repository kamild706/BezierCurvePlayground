import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSubject<T> implements Subject<T> {

    protected final Set<Observer<? super T>> mObservers = new HashSet<>();

    protected void dispatchingValue(T t) {
        mObservers.forEach(observer -> observer.onChanged(t));
    }

    @Override
    public void observe(@NotNull Observer<T> observer) {
        mObservers.add(observer);
    }
}
