package ServerApp;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ArrayLista<E> implements Iterable<E> {
    //HOLA
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;

        public ArrayLista() {
            elements = new Object[DEFAULT_CAPACITY];
        }

        public void add(E e) {
            if (size == elements.length) {
                ensureCapacity();
            }
            elements[size++] = e;
        }
        private void ensureCapacity() {
                int newSize = elements.length * 2;
                elements = Arrays.copyOf(elements, newSize);
            }
        @Override
        public Iterator<E> iterator() {
        return null;
        }

        @Override
        public void forEach(Consumer<? super E> action) {
        Iterable.super.forEach(action);
        }

        @Override
        public Spliterator<E> spliterator() {
            return Iterable.super.spliterator();
     }
    //return (E) elements[i];
}










