package tech.candy_dev.candykills.util.file.yaml;

import tech.candy_dev.candykills.CandyKills;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlFile {

    private final String name;
    private final String path;
    private final String folder;

    private final File cFile;

    private final YamlConfiguration config;

    public void saveConfig() {
        try {
            this.config.save(this.cFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlFile(String name, String path, String folder) {
        this.name = name;
        this.path = path;
        this.folder = folder;
        if (this.folder == null) {
            this.cFile = new File(this.path, this.name);
        } else {
            new File(this.path + File.separator + folder).mkdir();
            this.cFile = new File(this.path + File.separator + folder, this.name);
        }
        if (!this.cFile.exists()) {
            try {
                this.saveResource();
            } catch (IllegalArgumentException e) {
                try {
                    this.cFile.createNewFile();
                } catch (IOException ex) {
                }
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.cFile);
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }

    private void saveResource() {
        if (this.folder != null) {
            CandyKills.getInstance().saveResource(this.folder + File.separator + this.name, false);
        } else {
            CandyKills.getInstance().saveResource(this.name, false);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getFolder() {
        return this.folder;
    }

    public File getCFile() {
        return this.cFile;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }
    
}
