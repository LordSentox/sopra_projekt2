package de.sopra.javagame.model;


/**
 * @author Max Hauke Georg Bühmann, Melanie Arnds
 * <p>
 * ArtifactCards repräsentieren die Artefaktkarten des Spieles. Sie können folgende Typen annehmen:
 * {@link ArtifactCardType}
 */
public class ArtifactCard {

    private final ArtifactCardType type;

    public ArtifactCard(ArtifactCardType type) {
        this.type = type;
    }

    public ArtifactCardType getType() {
        return type;
    }
}
