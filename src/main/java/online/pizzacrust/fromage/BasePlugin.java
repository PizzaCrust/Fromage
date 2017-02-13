package online.pizzacrust.fromage;

import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.FilenameFilter;

import online.pizzacrust.fromage.api.Fromage;
import online.pizzacrust.fromage.api.LuaPlugin;
import online.pizzacrust.fromage.common.LuaObject;
import online.pizzacrust.fromage.common.env.LuaEnvironment;
import online.pizzacrust.fromage.common.log.CommonLogger;

/**
 * Represents a base to all implementations of {@link Fromage}.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public abstract class BasePlugin {

    /**
     * Retrieves the plugins dir of lua plugins.
     * @return
     */
    abstract File getLuaPluginsDir();

    /**
     * Retrieves the implementation of the logger.
     * @return
     */
    abstract CommonLogger getAbstractLogger();

    /**
     * Represents a common implementation of the start process of a plugin.
     */
    void pluginEnabled() {
        getAbstractLogger().info("Current classloader: {}", Thread.currentThread()
                .getContextClassLoader().getClass().getName());
        getAbstractLogger().info("Constructing the environment...");
        LuaEnvironment luaEnvironment = new LuaEnvironment();
        getAbstractLogger().info("Initialising the environment's scope...");
        luaEnvironment.initScope();
        getAbstractLogger().info("Creating/retrieving plugins directory...");
        getLuaPluginsDir().mkdirs();
        getAbstractLogger().info("Finding lua plugin files in the plugins directory...");
        File[] luaFiles = getLuaPluginsDir().listFiles((dir, name) -> name.endsWith(".lua"));
        getAbstractLogger().info("Found {} lua plugin files in the plugins directory!", String
                .valueOf(luaFiles != null ? luaFiles.length : new File[0].length));
        for (File luaFile : luaFiles != null ? luaFiles : new File[0]) {
            getAbstractLogger().info("Loading {} as a plugin file...", luaFile.getName());
            LuaValue chunk = LuaObject.GLOBAL_SCOPE.loadfile(luaFile.getAbsolutePath());
            chunk.call();
        }
        getAbstractLogger().info("Running initialising on plugins!");
        Fromage.PLUGINS.forEach(LuaPlugin::load);
        getAbstractLogger().info("Running #enable for all plugins!");
        Fromage.PLUGINS.forEach(LuaPlugin::enable);
    }

    /**
     * Represents a common implementation of the end process of a plugin.
     */
    void pluginDisabled() {
        getAbstractLogger().info("Running #disable for all plugins!");
        Fromage.PLUGINS.forEach(LuaPlugin::disable);
    }

}
