package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps;

import java.io.*;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashSetThreadSafe<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {

    private static final long serialVersionUID = -5024744406713321676L;
    private transient ConcurrentHashMap<E, Object> map;
    private static final Object PRESENT = new Object();

    public HashSetThreadSafe() {
        this.map = new ConcurrentHashMap<>();
    }

    public HashSetThreadSafe(Collection<? extends E> c) {
        this.map = new ConcurrentHashMap<>(Math.max((int) (c.size() / 0.75F) + 1, 16));
        this.addAll(c);
    }

    public HashSetThreadSafe(int initialCapacity, float loadFactor) {
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public HashSetThreadSafe(int initialCapacity) {
        this.map = new ConcurrentHashMap<>(initialCapacity);
    }

    public Iterator<E> iterator() {
        return this.map.keySet()
            .iterator();
    }

    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return this.map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return this.map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    public HashSetThreadSafe<E> clone() {
        try {
            HashSetThreadSafe<E> newSet = (HashSetThreadSafe<E>) super.clone();
            newSet.map = new ConcurrentHashMap<>(this.map);
            return newSet;
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.map.size());
        for (E e : this.map.keySet()) {
            s.writeObject(e);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.readFields();
        int size = s.readInt();
        if (size < 0) {
            throw new InvalidObjectException("Illegal size: " + size);
        }
        this.map = new ConcurrentHashMap<>();
        for (int i = 0; i < size; ++i) {
            E e = (E) s.readObject();
            this.map.put(e, PRESENT);
        }
    }

    @Override
    public Object[] toArray() {
        return this.map.keySet()
            .toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.map.keySet()
            .toArray(a);
    }
}
