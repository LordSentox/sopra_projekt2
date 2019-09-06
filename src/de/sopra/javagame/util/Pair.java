package de.sopra.javagame.util;

/**
 * Vereinigt zwei verschiedene Objekte, die zusammen eine eigene Bedeutung haben, zu einem.
 *
 * @param <L> Das erste, linke Objekt
 * @param <R> Das zweite, rechte Objekt
 */
public class Pair<L, R> {
    private L left;
    private R right;

    /**
     * Erstellt ein Paar aus den einzelnen Objekten
     *
     * @param left  Das linke Objekt
     * @param right Das rechte Objekt
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public void setRight(R right) {
        this.right = right;
    }
}
