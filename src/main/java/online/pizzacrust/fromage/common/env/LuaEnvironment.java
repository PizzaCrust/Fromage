package online.pizzacrust.fromage.common.env;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import online.pizzacrust.fromage.common.LuaObject;

/**
 * Represents the lua environment.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class LuaEnvironment {

    public static final LuaValue[] CORE_LIBRARIES = new LuaValue[] {
      new JseBaseLib(), new PackageLib(), new Bit32Lib(), new TableLib(), new StringLib(), new
            CoroutineLib(), new JseMathLib(), new JseIoLib(), new JseOsLib()
    };

    /**
     * Represents the environment's scope.
     */
    private final Globals globalScope = new Globals();

    public LuaEnvironment() {
        if (LuaObject.GLOBAL_SCOPE == null) {
            LuaObject.GLOBAL_SCOPE = globalScope;
        } else {
            throw new RuntimeException("System has created an additional environment, which " +
                    "cannot exist.");
        }
    }

    private boolean hasEmptyConstructor(Class<?> clazz) {
        try {
            clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    private boolean extendsStaticLib(Class<?> clazz) {
        return clazz.getSuperclass() == (LuaObject.Static.class);
    }

    private void processFromageLibs() {
        Reflections reflections = new Reflections();
        Set<Class<?>> possibleLibs = reflections.getTypesAnnotatedWith(FromageLib.class);
        List<Class<?>> validLibs = new ArrayList<>();
        possibleLibs.forEach((possibleLib) -> {
            if (hasEmptyConstructor(possibleLib) && extendsStaticLib(possibleLib)) {
                validLibs.add(possibleLib);
            }
        });
        validLibs.forEach((lib) -> {
            try {
                globalScope.load((LuaValue) lib.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private void processCoreLibs() {
        for (LuaValue coreLib : CORE_LIBRARIES) {
            globalScope.load(coreLib);
        }
    }

    private void installRuntime() {
        LoadState.install(globalScope);
        LuaC.install(globalScope);
    }

    private boolean init = false;

    public void initScope() {
        if (init) {
            throw new RuntimeException("You cannot init() a environment, two times!");
        }
        init = true;
        processCoreLibs();
        processFromageLibs();
        installRuntime();
    }

}
