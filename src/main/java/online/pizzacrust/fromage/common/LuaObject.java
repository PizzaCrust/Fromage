package online.pizzacrust.fromage.common;

import org.luaj.vm2.Globals;
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

    /**
     * For internal use. Throws a {@link RuntimeException}, if the specified parameter index
     * is invalid. The index starts at 1.
     * @param varargs
     * @param index
     */
    protected void checkParameter(Varargs varargs, int index) {
        if (varargs.isnil(index)) throw new RuntimeException();
    }

    /**
     * Represents a global scope for Lua.
     * Should be set at runtime.
     */
    public static Globals GLOBAL_SCOPE;

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
            if (method.isAnnotationPresent(LuaFunction.class) && method.isAnnotationPresent
                    (GlobalFunction.class)) {
                System.out.println(String.format("%s#%s is a local function and a global " +
                        "function! This is bad practice!", method.getDeclaringClass()
                        .getSimpleName(), method.getName()));
            }
            if (method.isAnnotationPresent(LuaFunction.class)) {
                String name = method.getName();
                addFunction(name, (varargs) -> {
                    try {
                        if (method.getParameterTypes().length >= 1) {
                            if (method.getParameterTypes()[0] == Varargs.class) {
                                Object returnVal = method.invoke(instance, varargs);
                                if (returnVal != null) {
                                    return (LuaValue) returnVal;
                                }
                            } else {
                                throw new RuntimeException("Invalid method description!");
                            }
                        } else {
                            Object returnVal = method.invoke(instance);
                            if (returnVal != null) {
                                return (LuaValue) returnVal;
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return LuaValue.NIL;
                });
            } else if (method.isAnnotationPresent(GlobalFunction.class)) {
                String name = method.getName();
                GLOBAL_SCOPE.set(name, new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs varargs) {
                        try {
                            if (method.getParameterTypes().length >= 1) {
                                if (method.getParameterTypes()[0] == Varargs.class) {
                                    Object returnVal = method.invoke(instance, varargs);
                                    if (returnVal != null) {
                                        return (LuaValue) returnVal;
                                    }
                                } else {
                                    throw new RuntimeException("Invalid method description!");
                                }
                            } else {
                                Object returnVal = method.invoke(instance);
                                if (returnVal != null) {
                                    return (LuaValue) returnVal;
                                }
                            }
                        }
                        catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return LuaValue.NIL;
                    }
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