package ServerApp;


import java.io.Serializable;
import java.util.Arrays;


public class ArrayLista<E> implements Serializable {
    //HOLA
    private transient int size = 0;
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
}










