package de.sopra.javagame.view.skin;

import de.sopra.javagame.model.WaterLevel;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.pdfsam.ui.FillProgressIndicator;

import static de.sopra.javagame.model.WaterLevel.MAX_WATER_LEVEL;

public class WaterLevelSkin implements Skin<FillProgressIndicator> {

    private static final String[] LABEL_COLOR_CLASSES = new String[]{"l-water", "lm-water", "m-water", "mh-water", "h-water"};
    private final FillProgressIndicator indicator;
    private final VBox container = new VBox();
    private final StackPane waterLevelContainer = new StackPane();
    private final Label levelLabel = new Label();
    private final Label cardsDrawnLabel = new Label();
    private final Rectangle clip = new Rectangle(waterLevelContainer.getWidth(), waterLevelContainer.getHeight());
    private final Circle borderCircle = new Circle();
    private final Circle labelCircle = new Circle();

    public WaterLevelSkin(FillProgressIndicator indicator) {
        this.indicator = indicator;
        this.initContainer();
        this.updateRadii();
        this.initStyles();

        this.clip.widthProperty().bind(waterLevelContainer.widthProperty());
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
        this.waterLevelContainer.getChildren().addAll(circlePane, this.borderCircle, this.labelCircle, this.levelLabel);

        waterLevelContainer.applyCss();
        waterLevelContainer.layout();

        updateCardsDrawnLabel();

        container.getChildren().addAll(waterLevelContainer, cardsDrawnLabel);
        container.setAlignment(Pos.CENTER);

        container.applyCss();
        container.layout();

        updateRadii();
    }


    private void initContainer() {
        this.waterLevelContainer.getStylesheets().addAll(indicator.getStylesheets());
        this.waterLevelContainer.getStylesheets().add(getClass().getResource("/stylesheets/water-level.css").toExternalForm());
        this.waterLevelContainer.getStyleClass().add("water-level-container");
        this.waterLevelContainer.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.waterLevelContainer.setMaxWidth(Double.NEGATIVE_INFINITY);

        this.container.getStylesheets().addAll(indicator.getStylesheets());
        this.container.getStylesheets().add(getClass().getResource("/stylesheets/water-level.css").toExternalForm());
        this.container.getStyleClass().add("water-level-container-outer");
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
        this.cardsDrawnLabel.getStyleClass().add("cards-drawn-label");
        this.cardsDrawnLabel.setStyle("-fx-stroke: #000");
    }

    private void setProgressLabel(int value) {
        if (inRange(value, 0, MAX_WATER_LEVEL)) {
            this.levelLabel.setText(value < MAX_WATER_LEVEL ? String.format("%d", value + 1) : "☠");
            updateLabelColor();
            updateBorderColor();
            updateLabelCircleColor();
            updateCardsDrawnLabel();
        }
    }

    private void updateCardsDrawnLabel() {
        cardsDrawnLabel.setText("Flutkarten/Zug: " + WaterLevel.DRAW_AMOUNT_BY_LEVEL[indicator.getProgress()]);
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
        cardsDrawnLabel.getStyleClass().removeAll(LABEL_COLOR_CLASSES);
        cardsDrawnLabel.getStyleClass().add(LABEL_COLOR_CLASSES[(int) (indicator.getProgress() * (4 / (double) MAX_WATER_LEVEL))]);
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
