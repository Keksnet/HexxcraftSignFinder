package de.neo.signfinder;

import de.neo.signfinder.commands.SignFinderCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SignFinder extends JavaPlugin {

    public static SignFinder getInstance() {
        return getPlugin(SignFinder.class);
    }

    @Override
    public void onEnable() {
        PluginCommand cmd = getCommand("signfinder");
        if (cmd != null) {
            SignFinderCommand executor = new SignFinderCommand();
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (!Files.exists(Path.of(getDataFolder().getAbsolutePath(), "config.yml"))) {
            saveDefaultConfig();
        }
        if (!Files.exists(Path.of(getDataFolder().getAbsolutePath(), "signs.db"))) {
            try {
                Files.copy(getResource("signs.db"), Path.of(getDataFolder().getAbsolutePath(), "signs.db"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
