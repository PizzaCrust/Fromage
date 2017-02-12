package online.pizzacrust.fromage.api;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.fromage.common.GlobalFunction;
import online.pizzacrust.fromage.common.LuaObject;

/**
 * Represents the core API for Lua.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class Fromage extends LuaObject.Static {

    public static final List<LuaPlugin> PLUGINS = new ArrayList<>();

    @GlobalFunction
    public void newPlugin(Varargs varargs) {
        checkParameter(varargs, 1);
        checkParameter(varargs, 2);
        checkParameter(varargs, 3);
        checkParameter(varargs, 4);
        if (!varargs.isstring(1) || !varargs.isstring(2) || !isDouble(varargs, 3) || !varargs
                .isfunction(4)) {
            throw new RuntimeException("Invalid #newPlugin call!");
        }
        String name = varargs.arg(1).tojstring();
        String desc = varargs.arg(2).tojstring();
        double version = varargs.arg(3).todouble();
        LuaValue predicate = varargs.arg(4);
        PLUGINS.add(new LuaPlugin(name, desc, version, predicate));
    }

    private boolean isDouble(Varargs varargs, int integer) {
        return isDouble(varargs.arg(integer));
    }

    private boolean isDouble(LuaValue value) {
        try {
            value.checkdouble();
        } catch (LuaError error) {
            return false;
        }
        return true;
    }

}
