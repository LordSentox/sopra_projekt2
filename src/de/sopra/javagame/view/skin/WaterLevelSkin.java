package de.sopra.javagame.view.skin;

import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.pdfsam.ui.FillProgressIndicator;

import static de.sopra.javagame.model.WaterLevel.MAX_WATER_LEVEL;

public class WaterLevelSkin implements Skin<FillProgressIndicator> {

    private static final String[] LABEL_COLOR_CLASSES = new String[]{"l-water", "lm-water", "m-water", "mh-water", "h-water"};
    private final FillProgressIndicator indicator;
    private final StackPane container = new StackPane();
    private final Label levelLabel = new Label();
    private final Rectangle clip = new Rectangle(container.getWidth(), container.getHeight());
    private final Circle borderCircle = new Circle();
    private final Circle labelCircle = new Circle();

    public WaterLevelSkin(FillProgressIndicator indicator) {
        this.indicator = indicator;
        this.initContainer();
        this.updateRadii();
        this.initStyles();

        this.clip.widthProperty().bind(container.widthProperty());
        this.clip.setManaged(false);

        Circle fillerCircle = new Circle();
        fillerCircle.radiusProperty().bindBidirectional(indicator.innerCircleRadiusProperty());
        this.borderCircle.radiusProperty().bindBidirectional(indicator.innerCircleRadiusProperty());
        indicator.innerCircleRadiusProperty().addListener((o, oldVal, newVal) -> this.labelCircle.setRadius(newVal.doubleValue() / 3));

        Image img = TextureLoader.getWater();
        fillerCircle.setFill(new ImagePattern(img));

        StackPane circlePane = new StackPane(fillerCircle);
        this.indicator.progressProperty().addListener((o, oldVal, newVal) -> {
            this.setProgressLabel(newVal.intValue());
            this.clip.setHeight(circlePane.getHeight() * newVal.intValue() / (double) MAX_WATER_LEVEL);
            this.clip.setTranslateY(circlePane.getHeight() - clip.getHeight());
        });

        circlePane.setClip(clip);

        this.initLabel(indicator.getProgress());
        this.container.getChildren().addAll(circlePane, this.borderCircle, this.labelCircle, this.levelLabel);

        container.applyCss();
        container.layout();

        updateRadii();
    }


    private void initContainer() {
        this.container.getStylesheets().addAll(indicator.getStylesheets());
        this.container.getStylesheets().add(getClass().getResource("/stylesheets/water-level.css").toExternalForm());
        this.container.getStyleClass().add("water-level-container");
        this.container.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.container.setMaxWidth(Double.NEGATIVE_INFINITY);
    }

    private void initStyles() {
        this.borderCircle.getStyleClass().add("water-level-border-circle");
        this.labelCircle.getStyleClass().add("water-level-label-circle");
    }

    private void updateRadii() {
        indicator.innerCircleRadiusProperty().setValue(indicator.innerCircleRadiusProperty().doubleValue() + 1);
    }

    private void initLabel(int value) {
        this.setProgressLabel(value);
        this.levelLabel.getStyleClass().add("water-level-label");
    }

    private void setProgressLabel(int value) {
        if (inRange(value, 0, MAX_WATER_LEVEL)) {
            this.levelLabel.setText(value < MAX_WATER_LEVEL ? String.format("%d", value + 1) : "☠");
            updateLabelColor();
            updateBorderColor();
            updateLabelCircleColor();
        }
    }

    private void updateLabelCircleColor() {
        labelCircle.getStyleClass().removeAll(LABEL_COLOR_CLASSES);
        labelCircle.getStyleClass().add(LABEL_COLOR_CLASSES[(int) (indicator.getProgress() * (4 / (double) MAX_WATER_LEVEL))]);
    }

    private void updateBorderColor() {
        borderCircle.getStyleClass().removeAll(LABEL_COLOR_CLASSES);
        borderCircle.getStyleClass().add(LABEL_COLOR_CLASSES[(int) (indicator.getProgress() * (4 / (double) MAX_WATER_LEVEL))]);
    }

    private void updateLabelColor() {
        levelLabel.getStyleClass().removeAll(LABEL_COLOR_CLASSES);
        levelLabel.getStyleClass().add(LABEL_COLOR_CLASSES[(int) (indicator.getProgress() * (4 / (double) MAX_WATER_LEVEL))]);
    }

    /**
     * @return true, falls n >= lower und n <= upper, false sonst
     */
    private boolean inRange(int n, int lower, int upper) {
        return n >= lower && n <= upper;
    }

    public FillProgressIndicator getSkinnable() {
        return this.indicator;
    }

    public Node getNode() {
        return this.container;
    }

    public void dispose() {
    }
}
