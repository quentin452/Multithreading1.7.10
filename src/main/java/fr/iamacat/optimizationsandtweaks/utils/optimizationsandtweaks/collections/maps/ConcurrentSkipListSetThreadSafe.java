package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConcurrentSkipListSetThreadSafe<E> extends ConcurrentSkipListSet<E> {

    private static final long serialVersionUID = -2479143111061671589L;
    private final ConcurrentNavigableMap<E, java.lang.Object> m;

    public ConcurrentSkipListSetThreadSafe() {
        this.m = new ConcurrentSkipListMap();
    }

    public ConcurrentSkipListSetThreadSafe(Comparator<? super E> var1) {
        this.m = new ConcurrentSkipListMap(var1);
    }

    public ConcurrentSkipListSetThreadSafe(Collection<? extends E> var1) {
        this.m = new ConcurrentSkipListMap();
        this.addAll(var1);
    }

    public ConcurrentSkipListSetThreadSafe(SortedSet<E> var1) {
        this.m = new ConcurrentSkipListMap(var1.comparator());
        this.addAll(var1);
    }

    ConcurrentSkipListSetThreadSafe(ConcurrentNavigableMap<E, java.lang.Object> var1) {
        this.m = var1;
    }

    public int size() {
        return this.m.size();
    }

    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    public boolean contains(java.lang.Object var1) {
        return this.m.containsKey(var1);
    }

    public boolean add(E var1) {
        return this.m.putIfAbsent(var1, Boolean.TRUE) == null;
    }

    public boolean remove(java.lang.Object var1) {
        return this.m.remove(var1, Boolean.TRUE);
    }

    public void clear() {
        this.m.clear();
    }

    public Iterator<E> iterator() {
        return m.keySet()
            .iterator();
    }

    public Iterator<E> descendingIterator() {
        return m.descendingKeySet()
            .iterator();
    }

    public boolean equals(java.lang.Object var1) {
        if (var1 == this) {
            return true;
        } else if (!(var1 instanceof Set)) {
            return false;
        } else {
            Collection var2 = (Collection) var1;

            try {
                return this.containsAll(var2) && var2.containsAll(this);
            } catch (ClassCastException var4) {
                return false;
            } catch (NullPointerException var5) {
                return false;
            }
        }
    }

    public boolean removeAll(Collection<?> var1) {
        boolean var2 = false;

        for (Object var4 : var1) {
            if (this.remove(var4)) {
                var2 = true;
            }
        }

        return var2;
    }

    public E lower(E var1) {
        return this.m.lowerKey(var1);
    }

    public E floor(E var1) {
        return this.m.floorKey(var1);
    }

    public E ceiling(E var1) {
        return this.m.ceilingKey(var1);
    }

    public E higher(E var1) {
        return this.m.higherKey(var1);
    }

    public E pollFirst() {
        Map.Entry var1 = this.m.pollFirstEntry();
        return var1 == null ? null : (E) var1.getKey();
    }

    public E pollLast() {
        Map.Entry var1 = this.m.pollLastEntry();
        return var1 == null ? null : (E) var1.getKey();
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

    public NavigableSet<E> subSet(E var1, boolean var2, E var3, boolean var4) {
        return new ConcurrentSkipListSetThreadSafe(this.m.subMap(var1, var2, var3, var4));
    }

    public NavigableSet<E> headSet(E var1, boolean var2) {
        return new ConcurrentSkipListSetThreadSafe(this.m.headMap(var1, var2));
    }

    public NavigableSet<E> tailSet(E var1, boolean var2) {
        return new ConcurrentSkipListSetThreadSafe(this.m.tailMap(var1, var2));
    }

    public NavigableSet<E> subSet(E var1, E var2) {
        return this.subSet(var1, true, var2, false);
    }

    public NavigableSet<E> headSet(E var1) {
        return this.headSet(var1, false);
    }

    public NavigableSet<E> tailSet(E var1) {
        return this.tailSet(var1, true);
    }

    public NavigableSet<E> descendingSet() {
        return new ConcurrentSkipListSetThreadSafe(this.m.descendingMap());
    }
}
