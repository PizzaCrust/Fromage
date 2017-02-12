package online.pizzacrust.fromage;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(id = "fromage")
public class FromageSpongePlugin extends BasePlugin {

    @Inject
    private Logger logger;

    @Override
    File getLuaPluginsDir() {
        return Sponge.getGame().getConfigManager().getPluginConfig(this).getDirectory().toFile();
    }

    @Listener
    public void onServerStart(GameStartingServerEvent e) {
        logger.info("Initialising Fromage...");
        logger.info("Using {} as a directory for loading Lua plugins.", getLuaPluginsDir().getPath());
        pluginEnabled();
    }

    @Listener
    public void onServerEnd(GameStoppingServerEvent e) {
        logger.info("Ending Fromage...");
        pluginDisabled();
    }

}
