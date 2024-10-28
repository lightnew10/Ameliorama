package fr.lightnew.ameliorama.tools;

import fr.lightnew.ameliorama.Ameliorama;

import java.io.File;
import java.io.IOException;

public class FileConfig {

    private String folder = Ameliorama.instance.getDataFolder().getAbsolutePath();
    public static File configWellOfSoul;

    public void defaultFiles() {
        configWellOfSoul = new File(folder, "well_of_soul");
        if (!configWellOfSoul.exists()) {
            try {
                configWellOfSoul.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
