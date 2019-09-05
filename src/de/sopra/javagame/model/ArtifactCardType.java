package de.sopra.javagame.model;

public enum ArtifactCardType {

    AIR(ArtifactType.AIR),
    EARTH(ArtifactType.EARTH),
    FIRE(ArtifactType.FIRE),
    WATER(ArtifactType.WATER),
    HELICOPTER,
    SANDBAGS,
    WATERS_RISE;

    private final ArtifactType type;

    ArtifactCardType(ArtifactType type) {
        this.type = type;
    }

    ArtifactCardType() {
        this.type = ArtifactType.NONE;
    }

    public String toString() {
        return null;
    }

    public ArtifactType toArtifactType() {
        return type;
    }

}
