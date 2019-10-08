package mainGame;

import java.util.function.Supplier;

public class RandomDifferentElement<T> implements Supplier<T> {
    private T[] source;
    private int lastIndex;

    @SafeVarargs
    public RandomDifferentElement(T... s) {
        source = s;
        lastIndex = -1;
    }

    @Override
    public T get() {
        if(source.length == 0) {
            return null;
        }
        else if(source.length == 1) {
            return source[0];
        }
        int l = lastIndex;
        while(l == lastIndex) {
            l = (int)(source.length*Math.random());
        }
        lastIndex = l;
        return source[l];
    }
}
