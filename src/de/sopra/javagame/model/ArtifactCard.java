package de.sopra.javagame.model;


/**
 * @author Max Hauke Georg Bühmann, Melanie Arnds
 * <p>
 * ArtifactCards repräsentieren die Artefaktkarten des Spieles. Sie können folgende Typen annehmen:
 * {@link ArtifactCardType}
 */
public class ArtifactCard implements Copyable<ArtifactCard> {

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
}
