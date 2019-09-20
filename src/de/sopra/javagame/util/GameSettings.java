package de.sopra.javagame.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.sopra.javagame.control.ControllerChan;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;

import java.io.*;

import static de.sopra.javagame.util.DebugUtil.debug;

/**
 *
 */
public class GameSettings {

    private ReadOnlyIntegerWrapper volumeMusic = new ReadOnlyIntegerWrapper(50);
    private ReadOnlyIntegerWrapper volumeEffects = new ReadOnlyIntegerWrapper(50);
    private ReadOnlyIntegerWrapper screenWidth = new ReadOnlyIntegerWrapper(1280);
    private ReadOnlyIntegerWrapper screenHeight = new ReadOnlyIntegerWrapper(720);

    private ReadOnlyBooleanWrapper devTools = new ReadOnlyBooleanWrapper(false);

    private static final boolean UWU = true;

    { if (UWU) {}}

    public void save() {
        SerializedSettings settings = new SerializedSettings(this);
        String serialized = settings.serialize();
        debug("Saved Settings: " + serialized);
    }


    public static GameSettings load() {
        SerializedSettings settings = SerializedSettings.deserialize();

        GameSettings gameSettings = new GameSettings();

        if (settings != null) {
            gameSettings.volumeMusic.set(settings.volumeMusic);
            gameSettings.volumeEffects.set(settings.volumeEffects);
            gameSettings.screenWidth.set(settings.screenWidth);
            gameSettings.screenHeight.set(settings.screenHeight);

            gameSettings.devTools.set(settings.devTools);
        }

        return gameSettings;
    }

    public void setVolumeMusic(int volumeMusic) {
        this.volumeMusic.set(volumeMusic);
    }

    public void setVolumeEffects(int volumeEffects) {
        this.volumeEffects.set(volumeEffects);
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth.set(screenWidth);
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight.set(screenHeight);
    }

    public void setDevTools(boolean devTools) {
        this.devTools.set(devTools);
    }

    public ReadOnlyIntegerWrapper getMusicVolume() {
        return volumeMusic;
    }

    public ReadOnlyIntegerWrapper getEffectsVolume() {
        return volumeEffects;
    }

    public ReadOnlyIntegerWrapper getScreenWidth() {
        return screenWidth;
    }

    public ReadOnlyIntegerWrapper getScreenHeight() {
        return screenHeight;
    }

    public ReadOnlyBooleanWrapper devToolsEnabled() {
        return devTools;
    }

    private static class SerializedSettings {

        private transient Gson gson = new GsonBuilder().setPrettyPrinting().create();

        private int volumeMusic = 50;
        private int volumeEffects = 50;
        private int screenWidth = 1280;
        private int screenHeight = 720;

        private boolean devTools = false;

        SerializedSettings() {
        }

        SerializedSettings(GameSettings settings) {
            volumeMusic = settings.volumeMusic.get();
            volumeEffects = settings.volumeEffects.get();
            screenWidth = settings.screenWidth.get();
            screenHeight = settings.screenHeight.get();
            devTools = settings.devTools.get();
        }

        private String serialize() {
            File settingsFile = ControllerChan.SETTINGS_FILE;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
                String serialized = gson.toJson(this);
                writer.write(serialized);
                return serialized;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        private static SerializedSettings deserialize() {
            File settingsFile = ControllerChan.SETTINGS_FILE;
            if (!settingsFile.exists()) {
                return null;
            }
            try (FileReader reader = new FileReader(settingsFile)) {
                return new Gson().fromJson(reader, SerializedSettings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
