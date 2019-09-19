package de.sopra.javagame.util;

import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class Map<T> {
    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 7;

    protected T[][] raw;

    public Map() {
        this.raw = newEmptyRaw();
    }

    Map(T[][] raw) throws IllegalArgumentException {
        if (raw == null || raw.length - 2 != SIZE_Y)
            throw new IllegalArgumentException();

        this.raw = this.newEmptyRaw();
        for (int y = 0; y < raw.length; ++y) {
            if (raw[y].length - 2 != SIZE_X)
                throw new IllegalArgumentException();

            System.arraycopy(raw[y], 0, this.raw[y], 0, raw[y].length);
        }
    }

    /**
     * Die Tile, welche an der übergebenen Position liegt wird zurückgegeben. Ist an der Stelle
     * kein Inselfeld wird <code>null</code> übergeben.
     *
     * @param position Die Position, von der man das Tile wissen möchte.
     * @return Tile an der Position
     */
    public T get(Point position) {
        return this.get(position.xPos, position.yPos);
    }


    /**
     * Die Tile, welche an der übergebenen Position liegt wird zurückgegeben. Ist an der Stelle
     * kein Inselfeld wird <code>null</code> übergeben.
     *
     * @return Tile an der Position
     */
    public T get(int posX, int posY) {
        if (posX >= SIZE_X || posY >= SIZE_Y)
            return null;
        if (this.raw.length >= posY + 1 || this.raw[posY + 1].length >= posX + 1)
            return null;
        return this.raw[posY + 1][posX + 1];
    }

    public void set(T value, Point position) {
        this.set(value, position.xPos, position.yPos);
    }

    /**
     * Ein for-each über alle Elemente der Map, die nicht <code>null</code> sind
     *
     * @param consumer die Funktion, welche alle Elemente der Map erhält, die nicht <code>null</code> sind
     */
    public void forEach(Consumer<T> consumer) {
        for (T[] elementRow : raw) {
            for (T element : elementRow) {
                if (element != null)
                    consumer.accept(element);
            }
        }
    }

    /**
     * Ein Stream über alle Elemente der Map, welche nicht <code>null</code> sind
     *
     * @return ein Stream über alle Elemente der Map ohne <code>null</code> -Elemente
     */
    public final Stream<T> stream() {
        Stream.Builder<T> builder = Stream.builder();
        forEach(builder::add);
        return builder.build();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Map) {
            Map<?> map = (Map<?>) other;
            for (int y = 0; y < Map.SIZE_Y; ++y) {
                for (int x = 0; x < Map.SIZE_X; ++x) {
                    if (this.get(x, y) == null && map.get(x, y) != null) {
                        return false;
                    }
                    if (this.get(x, y) != null && !this.get(x, y).equals(map.get(x, y))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void set(T value, int xPos, int yPos) {
        this.raw[yPos + 1][xPos + 1] = value;
    }

    public T[][] raw() {
        return this.raw;
    }

    protected abstract T[][] newEmptyRaw();

}
