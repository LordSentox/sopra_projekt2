package de.sopra.javagame.model;

import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CopyUtil;

import java.util.Objects;
import java.util.function.Function;

/**
 * MapTile beschreibt die einzelnen Kacheln, die das Spielfeld bilden
 *
 * @author Hermann "Roxi" Bühmann, Melanie Arnds
 */
public class MapTile implements Copyable<MapTile> {

    private String name;

    private PlayerType playerSpawn;

    private MapTileState state;

    private ArtifactType hiddenArtifact;

    public MapTile(String name, PlayerType playerSpawn, ArtifactType hiddenArtifact) {
        this.name = name;
        this.playerSpawn = playerSpawn;
        this.state = MapTileState.DRY;
        this.hiddenArtifact = hiddenArtifact;
    }

    /**
     * Erstellt ein neues MapTile vom übergebenen Wert number.
     *
     * @param number Der Index des gesuchten MapTiles. Muss eine Zahl zwischen 0 und 23 sein
     */
    public static MapTile fromNumber(int number) {
        if (number < 0 || number > 23) throw new IndexOutOfBoundsException();

        Function<Integer, MapTile> function = num -> {
            switch (num) {
                case  0: return new MapTile("Höhle der Schatten", PlayerType.NONE, ArtifactType.FIRE);
                case  1: return new MapTile("Landeplatz der Versager", PlayerType.PILOT, ArtifactType.NONE);
                case  2: return new MapTile("Klippen der Verzweiflung", PlayerType.NONE, ArtifactType.NONE);
                case  3: return new MapTile("Brücke des Verderbens", PlayerType.NONE, ArtifactType.NONE);
                case  4: return new MapTile("Pfad der Einsamkeit", PlayerType.NONE, ArtifactType.NONE);
                case  5: return new MapTile("Wald der Finsternis", PlayerType.NONE, ArtifactType.NONE);
                case  6: return new MapTile("Wächter der Insel", PlayerType.NONE, ArtifactType.NONE);
                case  7: return new MapTile("Sümpfe der Ruhe", PlayerType.NONE, ArtifactType.NONE);
                case  8: return new MapTile("Wächter der Sterne", PlayerType.NONE, ArtifactType.NONE);
                case  9: return new MapTile("Wüste der Entbehrung", PlayerType.NONE, ArtifactType.NONE);
                case 10: return new MapTile("Lagune des Lebens", PlayerType.NONE, ArtifactType.NONE);
                case 11: return new MapTile("Höhle des Grauens", PlayerType.NONE, ArtifactType.NONE);
                case 12: return new MapTile("Tor des Lichtes", PlayerType.NAVIGATOR, ArtifactType.NONE);
                case 13: return new MapTile("Tor der Dämmerung", PlayerType.DIVER, ArtifactType.NONE);
                case 14: return new MapTile("Tor der Sehnsucht", PlayerType.ENGINEER, ArtifactType.NONE);
                case 15: return new MapTile("Tor der Vergangenheit", PlayerType.EXPLORER, ArtifactType.NONE);
                case 16: return new MapTile("Tor des Vergessens", PlayerType.COURIER, ArtifactType.NONE);
                case 17: return new MapTile("Tempel des Mondes", PlayerType.NONE, ArtifactType.EARTH);
                case 18: return new MapTile("Tempel der Sonne", PlayerType.NONE, ArtifactType.EARTH);
                case 19: return new MapTile("Garten der Stille", PlayerType.NONE, ArtifactType.AIR);
                case 20: return new MapTile("Garten des Windes", PlayerType.NONE, ArtifactType.AIR);
                case 21: return new MapTile("Medusas Palast", PlayerType.NONE, ArtifactType.WATER);
                case 22: return new MapTile("Poseidons Palast", PlayerType.NONE, ArtifactType.WATER);
                case 23: return new MapTile("Höhle des Feuers", PlayerType.NONE, ArtifactType.FIRE);
                default: return null;
            }
        };
        return function.apply(number);
    }

    /**
     * setzt den state des MapTile von FLOODER auf DRY
     */
    void drain() {

    }

    /**
     * setzt den state des MapTile von DRY auf FLOODED oder von FLOODED auf GONE
     *
     * @return false wenn Fehler, true, sonst
     */
    public boolean flood() {
        return false;
    }

    public MapTileState getState() {
        return this.state;
    }

    void setState(MapTileState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public boolean hasPlayerSpawn() {
        return playerSpawn != PlayerType.NONE;
    }

    public boolean hasHiddenArtifact() {
        return hiddenArtifact != ArtifactType.NONE;
    }

    public ArtifactType getHiddenArtifact() {
        return hiddenArtifact;
    }

    public PlayerType getPlayerSpawn() {
        return playerSpawn;
    }

    @Override
    public MapTile copy() {
        MapTile mapTile = new MapTile(CopyUtil.copy(this.name), playerSpawn, hiddenArtifact);
        mapTile.state = this.state;
        return mapTile;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;

        MapTile tile = (MapTile) other;
        return Objects.equals(this.hiddenArtifact, tile.hiddenArtifact) &&
                Objects.equals(this.name, tile.name) &&
                Objects.equals(this.playerSpawn, tile.playerSpawn) &&
                Objects.equals(this.state, tile.state);
    }
}
