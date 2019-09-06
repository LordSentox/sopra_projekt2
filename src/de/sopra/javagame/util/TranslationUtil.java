package de.sopra.javagame.util;

import de.spaceparrots.translator.api.Translator;
import de.spaceparrots.translator.core.Language;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 06.09.2019
 * @since 06.09.2019
 */
public class TranslationUtil {

    public static Language jLANG = Translator.getDefaultLanguage();

    public static void setLanguage(String langKey) {
        jLANG = Translator.getLanguage(langKey);
    }

}