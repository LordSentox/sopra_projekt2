package de.sopra.javagame.view.command;

import de.spaceparrots.api.command.annotation.Command;
import de.spaceparrots.api.command.annotation.Option;
import de.spaceparrots.api.command.annotation.Senior;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 13.09.2019
 * @since 13.09.2019
 */
@Senior
public class DemonstrateOrdinaryRandomImplosionStrategies {

    @Command("doris")
    public void execute(@Option("mode") String mode) {
        if (mode == null || mode.isEmpty())
            mode = "normal";
        mode = mode.toLowerCase();
        switch (mode) {
            case "normal":
                normal();
                break;
            case "intense":
                intense();
                break;
            case "ultra":
                ultra();
                break;
        }
    }

    private void normal() {
        //TODO
    }

    private void intense() {
        //TODO
    }

    private void ultra() {
        //TODO
    }

}