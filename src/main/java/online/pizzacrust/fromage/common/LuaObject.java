package online.pizzacrust.fromage.common;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Represents a lua object.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class LuaObject extends LuaTable {

    private void addFunction(String name, Function<Varargs, LuaValue> func) {
        set(name, new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                LuaValue returnResult = func.apply(varargs);
                if (returnResult == null) {
                    return LuaValue.NIL;
                } else {
                    return returnResult;
                }
            }
        });
    }

    /**
     * This should be called in the constructor, as this
     * processes all the methods and automatically generates functions.
     */
    protected void processClass() {
        Object instance = this;
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(LuaFunction.class)) {
                String name = method.getName();
                addFunction(name, (varargs) -> {
                    try {
                        Object returnVal = method.invoke(instance, varargs);
                        if (returnVal != null) {
                            return (LuaValue) returnVal;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return LuaValue.NIL;
                });
            }
        }
    }

    /**
     * Represents {@link LuaObject}, however it can be referenced globally.
     *
     * @since 1.0-SNAPSHOT
     * @author PizzaCrust
     */
    public static abstract class Static extends LuaObject {

        @Override
        public LuaValue call(LuaValue modname, LuaValue env) {
            env.set(this.getClass().getSimpleName(), this);
            return this;
        }

    }

}