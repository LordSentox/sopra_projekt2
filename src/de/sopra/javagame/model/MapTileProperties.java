package de.sopra.javagame.model;

import de.sopra.javagame.model.player.PlayerType;

public enum MapTileProperties {
    CAVE_OF_SHADOWS   ("Höhle der Schatten", ArtifactType.FIRE, 0),
    FOOLS_LANDING     ("Landeplatz der Versager", PlayerType.PILOT, 1),
    CLIFFS_OF_ABANDON ("Klippen der Verzweiflung", 2),
    BREAKERS_BRIDGE   ("Brücke des Verderbens", 3),
    CRIMSON_FOREST    ("Pfad der Einsamkeit", 4),
    TWIGHLIGHT_HORROW ("Wald der Finsternis", 5),
    WATCHTOWER        ("Wächter der Insel", 6),
    MISTY_MARSH       ("Sümpfe der Ruhe", 7),
    OBSERVATORY       ("Wächter der Sterne", 8),
    DUNES_OF_DECEPTION("Wüste der Entbehrung", 9),
    LOST_LAGOON       ("Lagune des Lebens", 10),
    PHANTOM_ROCK      ("Höhle des Grauens", 11),
    GOLD_GATE         ("Tor des Lichtes", PlayerType.NAVIGATOR, 12),
    IRON_GATE         ("Tor der Dämmerung", PlayerType.DIVER, 13),
    BRONZE_GATE       ("Tor der Sehnsucht", PlayerType.ENGINEER, 14),
    COPPER_GATE       ("Tor der Vergangenheit", PlayerType.EXPLORER, 15),
    SILVER_GATE       ("Tor des Vergessens", PlayerType.COURIER, 16),
    TEMPLE_OF_THE_MOON("Tempel des Mondes", ArtifactType.EARTH, 17),
    TEMPLE_OF_THE_SUN ("Tempel der Sonne", ArtifactType.EARTH, 18),
    WHISPERING_GARDEN ("Garten der Stille", ArtifactType.AIR, 19),
    HOWLING_GARDEN    ("Garten des Windes", ArtifactType.AIR, 20),
    CORAL_PALACE      ("Medusas Palast", ArtifactType.WATER, 21),
    TIDAL_PALACE      ("Poseidons Palast", ArtifactType.WATER, 22),
    CAVE_OF_AMBERS    ("Höhle des Feuers", ArtifactType.FIRE, 23);

    String name;
    PlayerType spawn;
    ArtifactType hidden;
    int index;


    MapTileProperties(String name, int index) {
        this.name = name;
        this.spawn = PlayerType.NONE;
        this.hidden = ArtifactType.NONE;
        this.index = index;
    }

    MapTileProperties(String name, PlayerType spawn, int index) {
        this.name = name;
        this.spawn = spawn;
        this.hidden = ArtifactType.NONE;
        this.index = index;
    }

    MapTileProperties(String name, ArtifactType hidden, int index) {
        this.name = name;
        this.spawn = PlayerType.NONE;
        this.hidden = hidden;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public PlayerType getSpawn() {
        return spawn;
    }

    public ArtifactType getHidden() {
        return hidden;
    }

    public int getIndex() {
        return index;
    }

    public static MapTileProperties getByIndex(int index) {
        if (index < 0 || index > 23) throw new IndexOutOfBoundsException();

        return values()[index];
    }
}
