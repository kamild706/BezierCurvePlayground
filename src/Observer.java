@FunctionalInterface
public interface Observer<T> {

    void onChanged(T t);
}
