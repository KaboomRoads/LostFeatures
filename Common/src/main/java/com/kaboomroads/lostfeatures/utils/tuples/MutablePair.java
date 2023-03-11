package com.kaboomroads.lostfeatures.utils.tuples;

import java.util.Objects;

public class MutablePair<K, L> {
    private K first;
    private L second;

    public MutablePair(K first, L second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> MutablePair<A, B> of(A k, B l) {
        return new MutablePair<>(k, l);
    }

    public K getFirst() {
        return first;
    }

    public L getSecond() {
        return second;
    }

    public MutablePair<K, L> setFirst(K first) {
        this.first = first;
        return this;
    }

    public MutablePair<K, L> setSecond(L second) {
        this.second = second;
        return this;
    }

    public String toString() {
        return first + ", " + second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutablePair<?, ?> pair)) return false;
        return Objects.equals(getFirst(), pair.getFirst()) && Objects.equals(getSecond(), pair.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirst(), getSecond());
    }
}
