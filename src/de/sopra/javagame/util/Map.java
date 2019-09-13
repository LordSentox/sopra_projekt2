package de.sopra.javagame.util;

public abstract class Map<T> {
    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 7;

    T[][] raw;

    Map() {
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
            throw new IndexOutOfBoundsException();

        return this.raw[posY + 1][posX + 1];
    }

    public void set(T value, Point position) {
        this.set(value, position.xPos, position.yPos);
    }

    public void set(T value, int xPos, int yPos) {
        this.raw[xPos + 1][yPos + 1] = value;
    }

    public T[][] raw() {
        return this.raw;
    }

    protected abstract T[][] newEmptyRaw();
}
