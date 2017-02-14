package online.pizzacrust.fromage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;

import online.pizzacrust.fromage.common.LuaObject;
import online.pizzacrust.fromage.common.log.CommonLogger;
import online.pizzacrust.fromage.common.mc.Server;
import online.pizzacrust.fromage.sponge.SCommandLuaPlugins;
import online.pizzacrust.fromage.wrapper.sponge.SLF4JLogger;
import online.pizzacrust.fromage.wrapper.sponge.SpongeServer;

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

    private CommandSpec commandLuaPlugins() {
        return CommandSpec.builder().permission("fromage.luaplugins").description
                (Text.of("Lists all the plugins or find more info about a plugin!")).arguments
                (GenericArguments.optional(GenericArguments.string(Text.of("pluginName"))))
                .executor(new SCommandLuaPlugins()).build();
    }

    @Listener
    public void onServerStart(GameStartingServerEvent e) {
        LOGGER.info("Initialising Fromage...");
        LuaObject.CURRENT_IMPLEMENTATION = LuaObject.Implementation.SPONGE;
        Server.INSTANCE[0] = new SpongeServer();
        Sponge.getGame().getCommandManager().register(this, commandLuaPlugins(), "luaplugin",
                "lp", "lps", "luaplugins");
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
