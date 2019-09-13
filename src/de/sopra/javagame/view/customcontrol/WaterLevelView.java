package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.view.skin.WaterLevelSkin;
import javafx.scene.control.Skin;
import org.pdfsam.ui.FillProgressIndicator;

public class WaterLevelView extends FillProgressIndicator {

    public WaterLevelView() {
        getStyleClass().add("water-level");
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new WaterLevelSkin(this);
    }
}
