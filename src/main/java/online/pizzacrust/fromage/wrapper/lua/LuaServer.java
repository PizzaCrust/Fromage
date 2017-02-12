package online.pizzacrust.fromage.wrapper.lua;

import org.luaj.vm2.Varargs;

import java.lang.reflect.Method;

import online.pizzacrust.fromage.common.GlobalFunction;
import online.pizzacrust.fromage.common.LuaObject;
import online.pizzacrust.fromage.common.mc.Server;

/**
 * Implementation of {@link Server} using {@link Server#INSTANCE}.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class LuaServer extends LuaObject {

    private final Server server;

    public LuaServer(Server server) {
        this.processClass();
        this.server = server;
    }

    @GlobalFunction
    public void broadcast(Varargs varargs) {
        checkParameter(varargs, 1);
        String msg = varargs.tojstring();
        server.broadcast(msg);
    }

}
