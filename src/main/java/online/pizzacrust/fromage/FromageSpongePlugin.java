package online.pizzacrust.fromage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

import online.pizzacrust.fromage.common.log.CommonLogger;
import online.pizzacrust.fromage.wrapper.sponge.SLF4JLogger;

@Plugin(id = "fromage")
public class FromageSpongePlugin extends BasePlugin {

    public static final Logger LOGGER = LoggerFactory.getLogger("Fromage");

    @Override
    File getLuaPluginsDir() {
        return Sponge.getGame().getConfigManager().getPluginConfig(this).getDirectory().toFile();
    }

    @Override
    CommonLogger getAbstractLogger() {
        return new SLF4JLogger(LOGGER);
    }

    @Listener
    public void onServerStart(GameStartingServerEvent e) {
        LOGGER.info("Initialising Fromage...");
        LOGGER.info("Using {} as a directory for loading Lua plugins.", getLuaPluginsDir().getPath());
        LOGGER.info("Passing to abstraction...");
        pluginEnabled();
    }

    @Listener
    public void onServerEnd(GameStoppingServerEvent e) {
        LOGGER.info("Ending Fromage...");
        LOGGER.info("Passing to abstraction...");
        pluginDisabled();
    }

}
