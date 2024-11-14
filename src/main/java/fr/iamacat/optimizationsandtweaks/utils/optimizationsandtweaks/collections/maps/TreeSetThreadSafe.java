package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TreeSetThreadSafe<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable {

    private transient NavigableMap<E, Object> m;
    private static final Object PRESENT = new Object();
    private static final long serialVersionUID = -2479143000061671589L;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    TreeSetThreadSafe(NavigableMap<E, Object> m) {
        this.m = m;
    }

    public TreeSetThreadSafe() {
        this(new TreeMap<>());
    }

    public TreeSetThreadSafe(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
    }

    public TreeSetThreadSafe(Collection<? extends E> c) {
        this();
        this.addAll(c);
    }

    public TreeSetThreadSafe(SortedSet<E> s) {
        this(s.comparator());
        this.addAll(s);
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock()
            .lock();
        try {
            return new TreeSetThreadSafe<>(m.keySet()).iterator();
        } finally {
            lock.readLock()
                .unlock();
        }
    }

    public Iterator<E> descendingIterator() {
        lock.readLock()
            .lock();
        try {
            return m.descendingKeySet()
                .iterator();
        } finally {
            lock.readLock()
                .unlock();
        }
    }

    public NavigableSet<E> descendingSet() {
        return new TreeSetThreadSafe<>(this.m.descendingMap());
    }

    public int size() {
        return this.m.size();
    }

    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock()
            .lock();
        try {
            return m.containsKey(o);
        } finally {
            lock.readLock()
                .unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.writeLock()
            .lock();
        try {
            return m.put(e, PRESENT) == null;
        } finally {
            lock.writeLock()
                .unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock()
            .lock();
        try {
            return m.remove(o) == PRESENT;
        } finally {
            lock.writeLock()
                .unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock()
            .lock();
        try {
            m.clear();
        } finally {
            lock.writeLock()
                .unlock();
        }
    }

    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock()
            .lock();
        try {
            if (this.m.isEmpty() && !c.isEmpty() && c instanceof SortedSet) {
                NavigableMap<E, Object> map = new TreeMap<>(m.comparator());
                for (E element : c) {
                    map.put(element, PRESENT);
                }
                this.m = map;
                return true;
            }
            return super.addAll(c);
        } finally {
            lock.writeLock()
                .unlock();
        }
    }

    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return new TreeSetThreadSafe(this.m.subMap(fromElement, fromInclusive, toElement, toInclusive));
    }

    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return new TreeSetThreadSafe(this.m.headMap(toElement, inclusive));
    }

    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return new TreeSetThreadSafe(this.m.tailMap(fromElement, inclusive));
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
        return this.subSet(fromElement, true, toElement, false);
    }

    public SortedSet<E> headSet(E toElement) {
        return this.headSet(toElement, false);
    }

    public SortedSet<E> tailSet(E fromElement) {
        return this.tailSet(fromElement, true);
    }

    public Comparator<? super E> comparator() {
        return this.m.comparator();
    }

    public E first() {
        return this.m.firstKey();
    }

    public E last() {
        return this.m.lastKey();
    }

    public E lower(E e) {
        return this.m.lowerKey(e);
    }

    public E floor(E e) {
        return this.m.floorKey(e);
    }

    public E ceiling(E e) {
        return this.m.ceilingKey(e);
    }

    public E higher(E e) {
        return this.m.higherKey(e);
    }

    public E pollFirst() {
        Map.Entry<E, ?> e = this.m.pollFirstEntry();
        return e == null ? null : e.getKey();
    }

    public E pollLast() {
        Map.Entry<E, ?> e = this.m.pollLastEntry();
        return e == null ? null : e.getKey();
    }

    public Object clone() {
        TreeSetThreadSafe<E> clone;
        try {
            clone = (TreeSetThreadSafe<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }

        clone.m = new TreeMap<>(this.m);
        return clone;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(this.m.comparator());
        s.writeInt(this.m.size());

        for (E e : this.m.keySet()) {
            s.writeObject(e);
        }

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        Comparator<? super E> c = (Comparator<? super E>) s.readObject();
        TreeMap<E, Object> tm = new TreeMap<>(c);
        this.m = tm;
        int size = s.readInt();
        for (int i = 0; i < size; i++) {
            E element = (E) s.readObject();
            tm.put(element, PRESENT);
        }
    }

    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.DISTINCT);
    }
}
