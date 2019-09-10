package de.sopra.javagame.view.skin;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.pdfsam.ui.FillProgressIndicator;

public class WaterLevelSkin implements Skin<FillProgressIndicator> {

    private final FillProgressIndicator indicator;
    private final StackPane container = new StackPane();
    private final Label levelLabel = new Label();
    private final Rectangle cover = new Rectangle();
    private final Circle borderCircle = new Circle();
    private final Circle fillerCircle = new Circle();
    private final Circle labelCircle = new Circle();

    private static final int MAX_WATER_LEVEL = 7;
    private static final String[] LABEL_COLOR_CLASSES = new String[] {"l-water", "lm-water", "m-water", "mh-water", "h-water"};

    public WaterLevelSkin(FillProgressIndicator indicator) {
        this.indicator = indicator;
        this.initContainer(indicator);
        this.updateRadii();
        this.initStyles();

        AnchorPane coverPane = new AnchorPane();
        this.cover.getStyleClass().add("water-level-filler-cover");
        this.cover.widthProperty().bind(coverPane.widthProperty());
        this.cover.setManaged(false);

        Image img = new Image("/textures/water.jpg");
        fillerCircle.setFill(new ImagePattern(img));
        AnchorPane.setTopAnchor(this.cover, 0.0D);
        AnchorPane.setLeftAnchor(this.cover, 0.0D);
        AnchorPane.setRightAnchor(this.cover, 0.0D);

        coverPane.getChildren().add(this.cover);
        this.indicator.progressProperty().addListener((o, oldVal, newVal) -> {
            this.setProgressLabel(newVal.intValue());
            this.cover.setHeight(coverPane.getHeight() * ((double) MAX_WATER_LEVEL - newVal.intValue()) / (double) MAX_WATER_LEVEL);
        });
        this.indicator.innerCircleRadiusProperty().addListener(e -> this.updateRadii());
        coverPane.heightProperty().addListener((o, oldVal, newVal) -> this.cover.setHeight((double)newVal.intValue() * ((double) MAX_WATER_LEVEL - newVal.intValue()) / MAX_WATER_LEVEL));
        this.initLabel(indicator.getProgress());
        this.container.getChildren().addAll(this.fillerCircle, coverPane, this.borderCircle, this.labelCircle, this.levelLabel);
    }

    private void initContainer(FillProgressIndicator indicator) {
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
        this.fillerCircle.setRadius(this.indicator.getInnerCircleRadius());
        this.borderCircle.setRadius(this.indicator.getInnerCircleRadius());
        this.labelCircle.setRadius(this.indicator.getInnerCircleRadius() / 3);
    }

    private void initLabel(int value) {
        this.setProgressLabel(value);
        this.levelLabel.getStyleClass().add("water-level-label");
    }

    private void setProgressLabel(int value) {
        if (inRange(value, 0, MAX_WATER_LEVEL)) {
            this.levelLabel.setText(value < MAX_WATER_LEVEL ? String.format("%d", value) : "☠");
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

    public void dispose() { }
}