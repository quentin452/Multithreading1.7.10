package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ArrayListThreadSafe<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {

    private static final long serialVersionUID = 8683452581122892189L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = new Object[0];
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = new Object[0];
    private AtomicReferenceArray<Object> elementData;
    private final AtomicInteger size = new AtomicInteger(0);

    public ArrayListThreadSafe(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new AtomicReferenceArray<>(initialCapacity);
        } else {
            if (initialCapacity != 0) {
                throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
            }

            this.elementData = new AtomicReferenceArray<>(EMPTY_ELEMENTDATA);
        }
    }

    public ArrayListThreadSafe() {
        this.elementData = new AtomicReferenceArray<>(DEFAULTCAPACITY_EMPTY_ELEMENTDATA);
    }

    public ArrayListThreadSafe(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int size = a.length;
        this.size.set(size);
        this.modCount = 0;
        if (size != 0) {
            this.elementData = new AtomicReferenceArray<>(c.toArray());
        } else {
            this.elementData = new AtomicReferenceArray<>(EMPTY_ELEMENTDATA);
        }
    }

    public void trimToSize() {
        int currentSize = this.size.get();
        if (currentSize < this.elementData.length()) {
            AtomicReferenceArray<Object> newArray = new AtomicReferenceArray<>(currentSize);
            for (int i = 0; i < currentSize; i++) {
                newArray.set(i, this.elementData.get(i));
            }
            this.elementData.lazySet(0, newArray.get(0));
        }
    }

    public void ensureCapacity(int minCapacity) {
        while (minCapacity > this.elementData.length()
            && (this.elementData.get(0) != DEFAULTCAPACITY_EMPTY_ELEMENTDATA || minCapacity > 10)) {
            this.grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = elementData.length();
        int newCapacity = oldCapacity + Math.max(oldCapacity >> 1, 1);
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        AtomicReferenceArray<Object> newArray = new AtomicReferenceArray<>(newCapacity);
        for (int i = 0; i < oldCapacity; i++) {
            newArray.set(i, elementData.get(i));
        }
        elementData = newArray;
    }

    private Object[] grow() {
        int oldCapacity = elementData.length();
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            newArray[i] = elementData.get(i);
        }
        elementData = new AtomicReferenceArray<>(newArray);
        return newArray;
    }

    public int size() {
        return size.get();
    }

    @Override
    public boolean isEmpty() {
        return size.get() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        return this.indexOfRange(o, 0, size.get());
    }

    int indexOfRange(Object o, int start, int end) {
        AtomicReferenceArray<Object> es = this.elementData;
        int i;
        if (o == null) {
            for (i = start; i < end; ++i) {
                if (es.get(i) == null) {
                    return i;
                }
            }
        } else {
            for (i = start; i < end; ++i) {
                if (o.equals(es.get(i))) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.lastIndexOfRange(o, 0, size.get());
    }

    int lastIndexOfRange(Object o, int start, int end) {
        AtomicReferenceArray<Object> es = this.elementData;
        int i;
        if (o == null) {
            for (i = end - 1; i >= start; --i) {
                if (es.get(i) == null) {
                    return i;
                }
            }
        } else {
            for (i = end - 1; i >= start; --i) {
                if (o.equals(es.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Object clone() {
        try {
            ArrayListThreadSafe<?> v = (ArrayListThreadSafe) super.clone();
            AtomicReferenceArray<Object> newElementData = new AtomicReferenceArray<>(size.get());
            for (int i = 0; i < size.get(); i++) {
                newElementData.set(i, this.elementData.get(i));
            }
            v.elementData = newElementData;
            v.modCount = new AtomicInteger(0).intValue();
            return v;
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }

    @Override
    public Object[] toArray() {
        Object[] newArray = new Object[size.get()];
        for (int i = 0; i < size.get(); i++) {
            newArray[i] = elementData.get(i);
        }
        return newArray;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size.get()) {
            return (T[]) Arrays.copyOf(this.toArray(), size.get(), a.getClass());
        } else {
            System.arraycopy(this.toArray(), 0, a, 0, size.get());
            if (a.length > size.get()) {
                a[size.get()] = null;
            }
            return a;
        }
    }

    E elementData(int index) {
        return (E) this.elementData.get(index);
    }

    static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }

    public E get(int index) {
        checkIndex(index);
        return this.elementData(index);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size.get()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size.get());
        }
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldValue = (E) this.elementData.get(index);
        this.elementData.set(index, element);
        return oldValue;
    }

    @Override
    public boolean add(E e) {
        int s = size.getAndIncrement();
        if (s >= this.elementData.length()) {
            grow(s + 1);
        }
        this.elementData.set(s, e);
        return true;
    }

    @Override
    public void add(int index, E element) {
        this.rangeCheckForAdd(index);
        int s = size.getAndIncrement();
        if (s >= this.elementData.length()) {
            grow(s + 1);
        }
        for (int i = s; i > index; i--) {
            this.elementData.set(i, this.elementData.get(i - 1));
        }
        this.elementData.set(index, element);
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        int s = size.getAndDecrement();
        E oldValue = (E) this.elementData.get(index);
        for (int i = index; i < s - 1; i++) {
            this.elementData.set(i, this.elementData.get(i + 1));
        }
        this.elementData.set(s - 1, null); // Set the last element to null
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof List)) {
            return false;
        } else {
            int expectedModCount = this.modCount;
            boolean equal = o.getClass() == ArrayListThreadSafe.class ? this.equalsArrayList((ArrayListThreadSafe) o)
                : this.equalsRange((List) o, 0, size.get());
            this.checkForComodification(expectedModCount);
            return equal;
        }
    }

    boolean equalsRange(List<?> other, int from, int to) {
        Object[] es = new Object[this.elementData.length()];
        for (int i = 0; i < this.elementData.length(); i++) {
            es[i] = this.elementData.get(i);
        }
        if (to > es.length) {
            throw new ConcurrentModificationException();
        } else {
            Iterator oit;
            for (oit = other.iterator(); from < to; ++from) {
                if (!oit.hasNext() || !Objects.equals(es[from], oit.next())) {
                    return false;
                }
            }

            return !oit.hasNext();
        }
    }

    private boolean equalsArrayList(ArrayListThreadSafe<?> other) {
        int otherModCount = other.modCount;
        int s = this.size.get();
        boolean equal = false;

        if (s == other.size.get()) {
            equal = true;

            Object[] otherEs = new Object[other.elementData.length()];
            for (int i = 0; i < other.elementData.length(); i++) {
                otherEs[i] = other.elementData.get(i);
            }

            Object[] es = new Object[this.elementData.length()];
            for (int i = 0; i < this.elementData.length(); i++) {
                es[i] = this.elementData.get(i);
            }

            if (s > es.length || s > otherEs.length) {
                throw new ConcurrentModificationException();
            }

            for (int i = 0; i < s; ++i) {
                if (!Objects.equals(es[i], otherEs[i])) {
                    equal = false;
                    break;
                }
            }
        }

        other.checkForComodification(otherModCount);
        return equal;
    }

    private void checkForComodification(int expectedModCount) {
        if (this.modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    @Override
    public int hashCode() {
        int expectedModCount = this.modCount;
        int hash = this.hashCodeRange(0, size.get());
        this.checkForComodification(expectedModCount);
        return hash;
    }

    int hashCodeRange(int from, int to) {
        Object[] es = new Object[to - from];
        for (int i = from; i < to; i++) {
            es[i - from] = this.elementData.get(i);
        }

        int hashCode = 1;
        for (Object e : es) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }

        return hashCode;
    }

    @Override
    public boolean remove(Object o) {
        int size = this.size.get();
        for (int i = 0; i < size; i++) {
            Object e = this.elementData.get(i);
            if ((o == null && e == null) || (o != null && o.equals(e))) {
                this.fastRemove(i);
                return true;
            }
        }
        return false;
    }

    private void fastRemove(int i) {
        int newSize;
        if ((newSize = size.decrementAndGet()) > i) {
            for (int j = i; j < newSize; j++) {
                this.elementData.set(j, this.elementData.get(j + 1));
            }
        }
        this.elementData.set(newSize, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size.get(); i++) {
            this.elementData.set(i, null);
        }
        size.set(0);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) {
            return false;
        }
        int currentSize = this.size.get();
        while (numNew > this.elementData.length() - currentSize) {
            grow(currentSize + numNew);
        }
        for (int i = 0; i < numNew; i++) {
            this.elementData.set(currentSize + i, a[i]);
        }
        this.size.addAndGet(numNew);
        this.modCount++;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        this.rangeCheckForAdd(index);
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) {
            return false;
        } else {
            int s = size.get();
            Object[] elementData;
            if (numNew > (elementData = new Object[s + numNew]).length - s) {
                this.grow(s + numNew);
                elementData = copyAtomicArrayToArray(this.elementData, new Object[0]);
            }

            int numMoved = s - index;
            if (numMoved > 0) {
                System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
            }

            System.arraycopy(a, 0, elementData, index, numNew);
            size.addAndGet(numNew);
            ++this.modCount;
            return true;
        }
    }

    private static Object[] copyAtomicArrayToArray(AtomicReferenceArray<Object> atomicArray, Object[] array) {
        for (int i = 0; i < atomicArray.length(); i++) {
            array[i] = atomicArray.get(i);
        }
        return array;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(fromIndex, toIndex));
        } else {
            int numToRemove = toIndex - fromIndex;
            if (numToRemove > 0) {
                int s = size.get();
                int newSize = s - numToRemove;
                if (newSize < fromIndex) {
                    for (int i = fromIndex; i < s; i++) {
                        this.elementData.set(i, this.elementData.get(i + numToRemove));
                    }
                } else {
                    for (int i = 0; i < fromIndex; i++) {
                        this.elementData.set(i + numToRemove, this.elementData.get(i));
                    }
                    for (int i = toIndex; i < s; i++) {
                        this.elementData.set(i - numToRemove, this.elementData.get(i));
                    }
                }
                for (int i = newSize; i < s; i++) {
                    this.elementData.set(i, null);
                }
                size.set(newSize);
                ++this.modCount;
            }
        }
    }

    private void shiftTailOverGap(AtomicReferenceArray<Object> es, int lo, int hi) {
        int newSize = size.get() - (hi - lo);
        for (int i = hi; i < size.get(); i++) {
            es.set(i - (hi - lo), es.get(i));
        }
        for (int i = newSize; i < size.get(); i++) {
            es.set(i, null);
        }
        size.set(newSize);
    }

    private void rangeCheckForAdd(int index) {
        if (index > size.get() || index < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    private static String outOfBoundsMsg(int fromIndex, int toIndex) {
        return "From Index: " + fromIndex + " > To Index: " + toIndex;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.batchRemove(c, false, 0, size.get());
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.batchRemove(c, true, 0, size.get());
    }

    boolean batchRemove(Collection<?> c, boolean complement, int from, int end) {
        Objects.requireNonNull(c);
        AtomicReferenceArray<Object> es = this.elementData;

        for (int r = from; r != end; ++r) {
            if (c.contains(es.get(r)) != complement) {
                int w = r++;

                try {
                    for (; r < end; ++r) {
                        Object e;
                        if (c.contains(e = es.get(r)) == complement) {
                            es.set(w++, e);
                        }
                    }
                } catch (Throwable var12) {
                    while (r < end) {
                        es.set(w++, es.get(r++));
                    }
                    throw var12;
                } finally {
                    this.modCount += end - w;
                    this.shiftTailOverGap(es, w, end);
                }

                return true;
            }
        }

        return false;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        int expectedModCount = this.modCount;
        s.defaultWriteObject();
        s.writeInt(size.get());

        for (int i = 0; i < size.get(); ++i) {
            s.writeObject(this.elementData.get(i));
        }

        if (this.modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int size = s.readInt();

        if (size > 0) {
            Object[] elements = new Object[size];

            for (int i = 0; i < size; ++i) {
                elements[i] = s.readObject();
            }
            this.elementData = new AtomicReferenceArray<>(elements);
        } else {
            if (size != 0) {
                throw new InvalidObjectException("Invalid size: " + size);
            }
            this.elementData = new AtomicReferenceArray<>(0);
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        this.rangeCheckForAdd(index);
        return new ArrayListThreadSafe.ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ArrayListThreadSafe.ListItr(0);
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayListThreadSafe.Itr();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        return new ArrayListThreadSafe.SubList(this, fromIndex, toIndex);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        int expectedModCount = this.modCount;
        AtomicReferenceArray<Object> es = this.elementData;
        int size = this.size.get();

        for (int i = 0; this.modCount == expectedModCount && i < size; ++i) {
            action.accept(elementAt(es, i));
        }

        if (this.modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    @SuppressWarnings("unchecked")
    private E elementAt(AtomicReferenceArray<Object> array, int index) {
        return (E) array.get(index);
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListThreadSafe.ArrayListSpliterator(0, -1, 0);
    }

    private static long[] nBits(int n) {
        return new long[(n - 1 >> 6) + 1];
    }

    private static void setBit(long[] bits, int i) {
        bits[i >> 6] |= 1L << i;
    }

    private static boolean isClear(long[] bits, int i) {
        return (bits[i >> 6] & 1L << i) == 0L;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return this.removeIf(filter, 0, size.get());
    }

    boolean removeIf(Predicate<? super E> filter, int i, int end) {
        Objects.requireNonNull(filter);
        int expectedModCount;
        synchronized (this) {
            expectedModCount = this.modCount;
        }

        AtomicReferenceArray<Object> es = this.elementData;

        for (; i < end && !filter.test(elementAt(es, i)); ++i) {}

        if (i < end) {
            int beg = i;
            long[] deathRow = nBits(end - i);
            deathRow[0] = 1L;
            ++i;

            for (; i < end; ++i) {
                if (filter.test(elementAt(es, i))) {
                    setBit(deathRow, i - beg);
                }
            }

            synchronized (this) {
                if (this.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                } else {
                    this.modCount++;
                }
            }

            int w = beg;

            for (i = beg; i < end; ++i) {
                if (isClear(deathRow, i - beg)) {
                    es.set(w++, es.get(i));
                }
            }

            shiftTailOverGap(es, w, end);
            return true;
        } else {
            synchronized (this) {
                if (this.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
            return false;
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        this.replaceAllRange(operator, 0, size.get());
        ++this.modCount;
    }

    private void replaceAllRange(UnaryOperator<E> operator, int i, int end) {
        Objects.requireNonNull(operator);
        int expectedModCount;
        synchronized (this) {
            expectedModCount = this.modCount;
        }

        AtomicReferenceArray<Object> es = this.elementData;

        for (; i < end; ++i) {
            synchronized (this) {
                if (this.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
            es.set(i, operator.apply(elementAt(es, i)));
        }

        synchronized (this) {
            if (this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        int expectedModCount = this.modCount;
        E[] array;
        synchronized (this) {
            array = (E[]) new Object[size()];
            for (int i = 0; i < size(); i++) {
                array[i] = (E) this.elementData.get(i);
            }
        }
        Arrays.sort(array, c);

        synchronized (this) {
            if (this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                for (int i = 0; i < size(); i++) {
                    this.elementData.set(i, array[i]);
                }
                ++this.modCount;
            }
        }
    }

    void checkInvariants() {}

    private class ListItr extends ArrayListThreadSafe<E>.Itr implements ListIterator<E> {

        ListItr(int index) {
            super();
            this.cursor = index;
        }

        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public E previous() {
            this.checkForComodification();
            int i = this.cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            } else {
                AtomicReferenceArray<Object> elementData = ArrayListThreadSafe.this.elementData;
                if (i >= elementData.length()) {
                    throw new ConcurrentModificationException();
                } else {
                    this.cursor = i;
                    return (E) elementData.get(this.lastRet = i);
                }
            }
        }

        public void set(E e) {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            } else {
                this.checkForComodification();

                try {
                    ArrayListThreadSafe.this.set(this.lastRet, e);
                } catch (IndexOutOfBoundsException var3) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public void add(E e) {
            this.checkForComodification();

            try {
                int i = this.cursor;
                ArrayListThreadSafe.this.add(i, e);
                this.cursor = i + 1;
                this.lastRet = -1;
                this.expectedModCount = ArrayListThreadSafe.this.modCount;
            } catch (IndexOutOfBoundsException var3) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class Itr implements Iterator<E> {

        int cursor;
        int lastRet = -1;
        int expectedModCount;

        Itr() {
            this.expectedModCount = ArrayListThreadSafe.this.modCount;
        }

        public boolean hasNext() {
            return this.cursor != size.get();
        }

        public E next() {
            synchronized (ArrayListThreadSafe.this) {
                this.checkForComodification();
                int i = this.cursor;
                if (i >= size.get()) {
                    throw new NoSuchElementException();
                } else {
                    AtomicReferenceArray<Object> elementData = ArrayListThreadSafe.this.elementData;
                    if (i >= elementData.length()) {
                        throw new ConcurrentModificationException();
                    } else {
                        this.cursor = i + 1;
                        return (E) elementData.get(this.lastRet = i);
                    }
                }
            }
        }

        @Override
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            } else {
                this.checkForComodification();

                try {
                    ArrayListThreadSafe.this.remove(this.lastRet);
                    this.cursor = this.lastRet;
                    this.lastRet = -1;
                    this.expectedModCount = ArrayListThreadSafe.this.modCount;
                } catch (IndexOutOfBoundsException var2) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            int currentSize = size.get();
            int i = this.cursor;
            if (i < currentSize) {
                AtomicReferenceArray<Object> es = ArrayListThreadSafe.this.elementData;
                if (i >= es.length()) {
                    throw new ConcurrentModificationException();
                }

                while (i < currentSize && ArrayListThreadSafe.this.modCount == this.expectedModCount) {
                    action.accept((E) es.get(i));
                    ++i;
                }

                this.cursor = i;
                this.lastRet = i - 1;
                this.checkForComodification();
            }
        }

        final void checkForComodification() {
            if (ArrayListThreadSafe.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static class SubList<E> extends AbstractList<E> implements RandomAccess {

        private final ArrayListThreadSafe<E> root;
        private final ArrayListThreadSafe.SubList<E> parent;
        private final int offset;
        private int size;

        public SubList(ArrayListThreadSafe<E> root, int fromIndex, int toIndex) {
            this.root = root;
            this.parent = null;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = root.modCount;
        }

        private SubList(ArrayListThreadSafe.SubList<E> parent, int fromIndex, int toIndex) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = parent.modCount;
        }

        @Override
        public E set(int index, E element) {
            ArrayListThreadSafe<E> arrayList = new ArrayListThreadSafe<>();
            arrayList.checkIndex(index);
            this.checkForComodification();
            E oldValue = (E) this.root.elementData.get(this.offset + index);
            this.root.elementData.set(this.offset + index, element);
            return oldValue;
        }

        public E get(int index) {
            ArrayListThreadSafe<E> arrayList = new ArrayListThreadSafe<>();
            arrayList.checkIndex(index);
            this.checkForComodification();
            return this.root.elementData(this.offset + index);
        }

        public int size() {
            this.checkForComodification();
            return this.size;
        }

        @Override
        public void add(int index, E element) {
            this.rangeCheckForAdd(index);
            this.checkForComodification();
            this.root.add(this.offset + index, element);
            this.updateSizeAndModCount(1);
        }

        @Override
        public E remove(int index) {
            ArrayListThreadSafe<E> arrayList = new ArrayListThreadSafe<>();
            arrayList.checkIndex(index);
            this.checkForComodification();
            E result = this.root.remove(this.offset + index);
            this.updateSizeAndModCount(-1);
            return result;
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            this.checkForComodification();
            this.root.removeRange(this.offset + fromIndex, this.offset + toIndex);
            this.updateSizeAndModCount(fromIndex - toIndex);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return this.addAll(this.size, c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            this.rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize == 0) {
                return false;
            } else {
                this.checkForComodification();
                this.root.addAll(this.offset + index, c);
                this.updateSizeAndModCount(cSize);
                return true;
            }
        }

        @Override
        public void replaceAll(UnaryOperator<E> operator) {
            this.root.replaceAllRange(operator, this.offset, this.offset + this.size);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return this.batchRemove(c, false);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return this.batchRemove(c, true);
        }

        private boolean batchRemove(Collection<?> c, boolean complement) {
            this.checkForComodification();
            int oldSize = size;
            boolean modified = this.root.batchRemove(c, complement, this.offset, this.offset + this.size);
            if (modified) {
                this.updateSizeAndModCount(size - oldSize);
            }

            return modified;
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            this.checkForComodification();
            int oldSize = size;
            boolean modified = this.root.removeIf(filter, this.offset, this.offset + this.size);
            if (modified) {
                this.updateSizeAndModCount(size - oldSize);
            }

            return modified;
        }

        @Override
        public Object[] toArray() {
            this.checkForComodification();
            AtomicReferenceArray<Object> atomicArray = this.root.elementData;
            Object[] array = new Object[this.size];
            for (int i = 0; i < this.size; i++) {
                array[i] = atomicArray.get(this.offset + i);
            }
            return array;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            this.checkForComodification();
            int size = this.size;
            if (a.length < size) {
                @SuppressWarnings("unchecked")
                T[] newArray = (T[]) Array.newInstance(
                    a.getClass()
                        .getComponentType(),
                    size);
                for (int i = 0; i < size; i++) {
                    newArray[i] = (T) this.root.elementData.get(this.offset + i);
                }
                return newArray;
            } else {
                System.arraycopy(this.root.elementData, this.offset, a, 0, size);
                if (a.length > size) {
                    a[size] = null;
                }
                return a;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof List)) {
                return false;
            } else {
                boolean equal = this.root.equalsRange((List) o, this.offset, this.offset + this.size);
                this.checkForComodification();
                return equal;
            }
        }

        @Override
        public int hashCode() {
            int hash = this.root.hashCodeRange(this.offset, this.offset + this.size);
            this.checkForComodification();
            return hash;
        }

        @Override
        public int indexOf(Object o) {
            int index = this.root.indexOfRange(o, this.offset, this.offset + this.size);
            this.checkForComodification();
            return index >= 0 ? index - this.offset : -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            int index = this.root.lastIndexOfRange(o, this.offset, this.offset + this.size);
            this.checkForComodification();
            return index >= 0 ? index - this.offset : -1;
        }

        @Override
        public boolean contains(Object o) {
            return this.indexOf(o) >= 0;
        }

        @Override
        public Iterator<E> iterator() {
            return this.listIterator();
        }

        @Override
        public ListIterator<E> listIterator(final int index) {
            this.checkForComodification();
            this.rangeCheckForAdd(index);
            return new ListIterator<E>() {

                int cursor = index;
                int lastRet = -1;
                int expectedModCount;

                {
                    this.expectedModCount = ArrayListThreadSafe.SubList.this.modCount;
                }

                public boolean hasNext() {
                    return this.cursor != ArrayListThreadSafe.SubList.this.size;
                }

                public E next() {
                    this.checkForComodification();
                    int i = this.cursor;
                    if (i >= ArrayListThreadSafe.SubList.this.size) {
                        throw new NoSuchElementException();
                    } else {
                        Object[] elementData = new AtomicReferenceArray[] { SubList.this.root.elementData };
                        if (ArrayListThreadSafe.SubList.this.offset + i >= elementData.length) {
                            throw new ConcurrentModificationException();
                        } else {
                            this.cursor = i + 1;
                            return (E) elementData[SubList.this.offset + (this.lastRet = i)];
                        }
                    }
                }

                public boolean hasPrevious() {
                    return this.cursor != 0;
                }

                public E previous() {
                    this.checkForComodification();
                    int i = this.cursor - 1;
                    if (i < 0) {
                        throw new NoSuchElementException();
                    } else {
                        Object[] elementData = new AtomicReferenceArray[] { SubList.this.root.elementData };
                        if (ArrayListThreadSafe.SubList.this.offset + i >= elementData.length) {
                            throw new ConcurrentModificationException();
                        } else {
                            this.cursor = i;
                            return (E) elementData[SubList.this.offset + (this.lastRet = i)];
                        }
                    }
                }

                @Override
                public void forEachRemaining(Consumer<? super E> action) {
                    Objects.requireNonNull(action);
                    int size = ArrayListThreadSafe.SubList.this.size;
                    int i = this.cursor;
                    if (i < size) {
                        Object[] es = new AtomicReferenceArray[] { SubList.this.root.elementData };
                        if (ArrayListThreadSafe.SubList.this.offset + i >= es.length) {
                            throw new ConcurrentModificationException();
                        }

                        while (i < size && ArrayListThreadSafe.SubList.this.root.modCount == this.expectedModCount) {
                            action
                                .accept(ArrayListThreadSafe.elementAt(es, ArrayListThreadSafe.SubList.this.offset + i));
                            ++i;
                        }

                        this.cursor = i;
                        this.lastRet = i - 1;
                        this.checkForComodification();
                    }

                }

                public int nextIndex() {
                    return this.cursor;
                }

                public int previousIndex() {
                    return this.cursor - 1;
                }

                public void remove() {
                    if (this.lastRet < 0) {
                        throw new IllegalStateException();
                    } else {
                        this.checkForComodification();

                        try {
                            ArrayListThreadSafe.SubList.this.remove(this.lastRet);
                            this.cursor = this.lastRet;
                            this.lastRet = -1;
                            this.expectedModCount = ArrayListThreadSafe.SubList.this.modCount;
                        } catch (IndexOutOfBoundsException var2) {
                            throw new ConcurrentModificationException();
                        }
                    }
                }

                public void set(E e) {
                    if (this.lastRet < 0) {
                        throw new IllegalStateException();
                    } else {
                        this.checkForComodification();

                        try {
                            ArrayListThreadSafe.SubList.this.root
                                .set(ArrayListThreadSafe.SubList.this.offset + this.lastRet, e);
                        } catch (IndexOutOfBoundsException var3) {
                            throw new ConcurrentModificationException();
                        }
                    }
                }

                public void add(E e) {
                    this.checkForComodification();

                    try {
                        int i = this.cursor;
                        ArrayListThreadSafe.SubList.this.add(i, e);
                        this.cursor = i + 1;
                        this.lastRet = -1;
                        this.expectedModCount = ArrayListThreadSafe.SubList.this.modCount;
                    } catch (IndexOutOfBoundsException var3) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (ArrayListThreadSafe.SubList.this.root.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }
            };
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
                throw new IndexOutOfBoundsException();
            }
            return new ArrayListThreadSafe.SubList(this, fromIndex, toIndex);
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size) {
                throw new IndexOutOfBoundsException(this.outOfBoundsMsg(index));
            }
        }

        private String outOfBoundsMsg(int index) {
            return "Index: " + index + ", Size: " + this.size;
        }

        private void checkForComodification() {
            if (this.root.modCount != this.modCount) {
                throw new ConcurrentModificationException();
            }
        }

        private void updateSizeAndModCount(int sizeChange) {
            ArrayListThreadSafe.SubList<E> slist = this;

            do {
                slist.size += sizeChange;
                slist.modCount = this.root.modCount;
                slist = slist.parent;
            } while (slist != null);

        }

        @Override
        public Spliterator<E> spliterator() {
            this.checkForComodification();
            return new Spliterator<E>() {

                private int index;
                private int fence;
                private int expectedModCount;

                {
                    this.index = ArrayListThreadSafe.SubList.this.offset;
                    this.fence = -1;
                }

                private int getFence() {
                    int hi;
                    if ((hi = this.fence) < 0) {
                        this.expectedModCount = ArrayListThreadSafe.SubList.this.modCount;
                        hi = this.fence = ArrayListThreadSafe.SubList.this.offset
                            + ArrayListThreadSafe.SubList.this.size;
                    }

                    return hi;
                }

                public ArrayListThreadSafe<E>.ArrayListSpliterator trySplit() {
                    int hi = this.getFence();
                    int lo = this.index;
                    int mid = lo + hi >>> 1;
                    ArrayListThreadSafe.ArrayListSpliterator var10000;
                    if (lo >= mid) {
                        var10000 = null;
                    } else {
                        ArrayListThreadSafe var10002 = ArrayListThreadSafe.SubList.this.root;
                        Objects.requireNonNull(var10002);
                        var10000 = var10002.new ArrayListSpliterator(lo, this.index = mid, this.expectedModCount);
                    }

                    return var10000;
                }

                public boolean tryAdvance(Consumer<? super E> action) {
                    Objects.requireNonNull(action);
                    int hi = this.getFence();
                    int i = this.index;
                    if (i < hi) {
                        this.index = i + 1;
                        AtomicReferenceArray<Object> elementData = ArrayListThreadSafe.SubList.this.root.elementData;
                        @SuppressWarnings("unchecked")
                        E e = (E) elementData.get(i);
                        action.accept(e);
                        if (ArrayListThreadSafe.SubList.this.root.modCount != this.expectedModCount) {
                            throw new ConcurrentModificationException();
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }

                @Override
                public void forEachRemaining(Consumer<? super E> action) {
                    Objects.requireNonNull(action);
                    ArrayListThreadSafe<E> lst = ArrayListThreadSafe.SubList.this.root;
                    Object[] a;
                    if ((a = new AtomicReferenceArray[] { lst.elementData }) != null) {
                        int hi;
                        int mc;
                        if ((hi = this.fence) < 0) {
                            mc = ArrayListThreadSafe.SubList.this.modCount;
                            hi = ArrayListThreadSafe.SubList.this.offset + ArrayListThreadSafe.SubList.this.size;
                        } else {
                            mc = this.expectedModCount;
                        }

                        int i;
                        if ((i = this.index) >= 0 && (this.index = hi) <= a.length) {
                            while (i < hi) {
                                E e = (E) a[i];
                                action.accept(e);
                                ++i;
                            }

                            if (lst.modCount == mc) {
                                return;
                            }
                        }
                    }

                    throw new ConcurrentModificationException();
                }

                public long estimateSize() {
                    return this.getFence() - this.index;
                }

                public int characteristics() {
                    return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
                }
            };
        }
    }

    final class ArrayListSpliterator implements Spliterator<E> {

        private int index;
        private int fence;
        private int expectedModCount;

        ArrayListSpliterator(int origin, int fence, int expectedModCount) {
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() {
            int hi;
            if ((hi = this.fence) < 0) {
                this.expectedModCount = ArrayListThreadSafe.this.modCount;
                hi = this.fence = size.get();
            }

            return hi;
        }

        public ArrayListThreadSafe<E>.ArrayListSpliterator trySplit() {
            int hi = this.getFence();
            int lo = this.index;
            int mid = lo + hi >>> 1;
            return lo >= mid ? null
                : ArrayListThreadSafe.this.new ArrayListSpliterator(lo, this.index = mid, this.expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null) {
                throw new NullPointerException();
            } else {
                int hi = this.getFence();
                int i = this.index;
                if (i < hi) {
                    this.index = i + 1;
                    Object[] elementData = new AtomicReferenceArray[] { ArrayListThreadSafe.this.elementData };
                    E e = (E) elementData[i];
                    action.accept(e);
                    if (ArrayListThreadSafe.this.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            if (action == null) {
                throw new NullPointerException();
            }

            AtomicReferenceArray<Object> a = ArrayListThreadSafe.this.elementData;
            int hi;
            int mc;
            if (a != null && (hi = ArrayListThreadSafe.this.size.get()) >= 0) {
                if ((mc = ArrayListThreadSafe.this.modCount) < 0) {
                    mc = ArrayListThreadSafe.this.modCount;
                }

                int i = this.index;
                if (i >= 0 && (this.index = hi) <= a.length()) {
                    while (i < hi) {
                        @SuppressWarnings("unchecked")
                        E e = (E) a.get(i);
                        if (e != null) {
                            action.accept(e);
                        }
                        ++i;
                    }

                    if (ArrayListThreadSafe.this.modCount == mc) {
                        return;
                    }
                }
            }

            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return this.getFence() - this.index;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }
}
