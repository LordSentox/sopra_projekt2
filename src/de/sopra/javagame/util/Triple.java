package de.sopra.javagame.util;

/**
 * <h1>SpaceParrots Utils</h1>
 * A class for creating a simple relation between three values.
 *
 * @author Julius Korweck
 * @version 16.09.2019
 * @since 12.02.2017
 */
public final class Triple<A, B, C> {

    private A first;
    private B second;
    private C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public final Pair<A, B> getFirstPair() {
        return new Pair<>(getFirst(), getSecond());
    }

    public final Pair<B, C> getLastPair() {
        return new Pair<>(getSecond(), getThird());
    }

    public final Pair<A, C> getOuterPair() {
        return new Pair<>(getFirst(), getThird());
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public void setThird(C third) {
        this.third = third;
    }
}
/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2019
 *
 ***********************************************************************************************/