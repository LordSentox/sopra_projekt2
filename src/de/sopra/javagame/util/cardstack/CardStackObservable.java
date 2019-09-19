package de.sopra.javagame.util.cardstack;

import de.sopra.javagame.model.Copyable;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 10.09.2019
 * @since 10.09.2019
 */
public abstract class CardStackObservable<T extends Copyable<T>> {

    private CardStackObserver<T> observer;

    public final void setObserver(CardStackObserver<T> observer) {
        this.observer = observer;
    }

    public CardStackObserver<T> getObserver() {
        return observer;
    }

    public final void callNotify(CardStack<T> cardStack, CardStackChange<T> change) {
        if (this.observer != null) {
            this.observer.update(cardStack, change);
        }
    }

    public final void notifyRemove(CardStack<T> cardStack, T element, boolean discardStack) {
        callNotify(cardStack, new CardStackChange<T>() {
            @Override
            public boolean changedDiscardPile() {
                return discardStack;
            }

            @Override
            public T removedElement() {
                return element;
            }
        });
    }

    public final void notifyAdd(CardStack<T> cardStack, T element, boolean discardStack) {
        callNotify(cardStack, new CardStackChange<T>() {
            @Override
            public boolean changedDiscardPile() {
                return discardStack;
            }

            @Override
            public T newElement() {
                return element;
            }
        });
    }

}