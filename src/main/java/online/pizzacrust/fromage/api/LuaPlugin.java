package online.pizzacrust.fromage.api;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import online.pizzacrust.fromage.common.LuaFunction;
import online.pizzacrust.fromage.common.LuaObject;

/**
 * Represents a lua plugin.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class LuaPlugin extends LuaObject {

    private final String name;
    private final String description;
    private final double version;

    private final LuaValue handleFunc;
    private LuaValue enableFunc;
    private LuaValue disableFunc;

    private boolean loaded = false;
    private boolean enabled = false;

    public LuaPlugin(String name, String description, double version, LuaValue predicate) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.handleFunc = predicate;
        this.processClass();
    }

    @LuaFunction
    public LuaValue getName(Varargs varargs) {
        return valueOf(name);
    }

    @LuaFunction
    public LuaValue getVersion(Varargs varargs) {
        return valueOf(version);
    }

    @LuaFunction
    public LuaValue getDescription(Varargs varargs) {
        return valueOf(description);
    }

    @LuaFunction
    public void onEnable(Varargs varargs) {
        checkParameter(varargs, 1);
        LuaValue possiblePredicate = varargs.arg(1);
        if (!possiblePredicate.isfunction()) {
            throw new RuntimeException("Given function is not a valid predicate!");
        }
        this.enableFunc = possiblePredicate;
    }

    @LuaFunction
    public void onDisable(Varargs varargs) {
        checkParameter(varargs, 1);
        LuaValue possiblePredicate = varargs.arg(1);
        if (!possiblePredicate.isfunction()) {
            throw new RuntimeException("Given function is not a valid predicate!");
        }
        this.disableFunc = possiblePredicate;
    }

    public void load() {
        if (!loaded) {
            handleFunc.call(this);
            loaded = true;
        } else {
            throw new RuntimeException("Attempting to load a plugin twice!");
        }
    }

    public void enable() {
        if (enableFunc == null || !loaded || enabled) {
            throw new RuntimeException("Enable function is null! Set the enable function in " +
                    "#onEnable!");
        }
        enableFunc.call();
        enabled = true;
    }

    public void disable() {
        if (disableFunc == null || !loaded || !enabled) {
            throw new RuntimeException("Disable function is null! Set the disable function in " +
                    "#onDisable!");
        }
        disableFunc.call();
        enabled = false;
    }

}