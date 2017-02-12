package online.pizzacrust.fromage;

import java.io.File;
import java.io.FilenameFilter;

import online.pizzacrust.fromage.api.Fromage;
import online.pizzacrust.fromage.api.LuaPlugin;
import online.pizzacrust.fromage.common.LuaObject;
import online.pizzacrust.fromage.common.env.LuaEnvironment;

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
     * Represents a common implementation of the start process of a plugin.
     */
    protected void pluginEnabled() {
        LuaEnvironment luaEnvironment = new LuaEnvironment();
        luaEnvironment.initScope();
        getLuaPluginsDir().mkdirs();
        File[] luaFiles = getLuaPluginsDir().listFiles((dir, name) -> name.endsWith(".lua"));
        for (File luaFile : luaFiles != null ? luaFiles : new File[0]) {
            LuaObject.GLOBAL_SCOPE.loadfile(luaFile.getAbsolutePath());
        }
        Fromage.PLUGINS.forEach(LuaPlugin::enable);
    }

    /**
     * Represents a common implementation of the end process of a plugin.
     */
    protected void pluginDisabled() {
        Fromage.PLUGINS.forEach(LuaPlugin::disable);
    }

}
