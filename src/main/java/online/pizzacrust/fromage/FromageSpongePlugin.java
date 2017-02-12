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

    private Logger logger = LoggerFactory.getLogger("Fromage");

    @Override
    File getLuaPluginsDir() {
        return Sponge.getGame().getConfigManager().getPluginConfig(this).getDirectory().toFile();
    }

    @Override
    CommonLogger getAbstractLogger() {
        return new SLF4JLogger(logger);
    }

    @Listener
    public void onServerStart(GameStartingServerEvent e) {
        logger.info("Initialising Fromage...");
        logger.info("Using {} as a directory for loading Lua plugins.", getLuaPluginsDir().getPath());
        logger.info("Passing to abstraction...");
        pluginEnabled();
    }

    @Listener
    public void onServerEnd(GameStoppingServerEvent e) {
        logger.info("Ending Fromage...");
        logger.info("Passing to abstraction...");
        pluginDisabled();
    }

}
