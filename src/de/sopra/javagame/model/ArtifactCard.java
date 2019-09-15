package de.sopra.javagame.model;


import java.io.Serializable;

/**
 * @author Max Hauke Georg Bühmann, Melanie Arnds
 * <p>
 * ArtifactCards repräsentieren die Artefaktkarten des Spieles. Sie können folgende Typen annehmen:
 * {@link ArtifactCardType}
 */
public class ArtifactCard implements Copyable<ArtifactCard>, Serializable {

    private final ArtifactCardType type;

    public ArtifactCard(ArtifactCardType type) {
        this.type = type;
    }

    public ArtifactCardType getType() {
        return type;
    }

    @Override
    public ArtifactCard copy() {
        return new ArtifactCard(type);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ArtifactCard that = (ArtifactCard) other;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type.ordinal();
    }
}
