package online.pizzacrust.fromage;

import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

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

    private File getLibrariesDir() {
        return new File(getLuaPluginsDir(), "libraries");
    }

    private void addURL(URL url) {
        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread()
                .getContextClassLoader();
        Class<?> currentLoader = Thread.currentThread().getContextClassLoader().getClass();
        try {
            Method method = currentLoader.getMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(urlClassLoader, url);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void download(URL url, File file) throws IOException {
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
    }

    /**
     * Represents a common implementation of the start process of a plugin.
     */
    void pluginEnabled() {
        getAbstractLogger().info("Creating/retrieving plugins directory...");
        getLuaPluginsDir().mkdirs();
        getAbstractLogger().info("Creating/retrieving libraries directory...");
        if (!getLibrariesDir().exists()) {
            getLibrariesDir().mkdirs();
            try {
                URL reflections = new URL("http://central.maven" +
                        ".org/maven2/org/reflections/reflections/0.9.10/reflections-0.9.10.jar");
                URL javaassist = new URL("http://central.maven" +
                        ".org/maven2/org/javassist/javassist/3.18.2-GA/javassist-3.18.2-GA.jar");
                URL luaj = new URL("http://central.maven" +
                        ".org/maven2/org/luaj/luaj-jse/3.0.1/luaj-jse-3.0.1.jar");
                File reflectionsJar = new File(getLibrariesDir(), "reflections-lib.jar");
                File javaassistJar = new File(getLibrariesDir(), "javaassist-lib.jar");
                File luajJar = new File(getLibrariesDir(),"luaj.jar");
                getAbstractLogger().info("Downloading reflections...");
                download(reflections, reflectionsJar);
                getAbstractLogger().info("Downloading javaassist...");
                download(javaassist, javaassistJar);
                getAbstractLogger().info("Downloading luaj...");
                download(luaj, luajJar);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            getAbstractLogger().info("Loading libraries...");
            File[] libraryFiles = getLibrariesDir().listFiles((dir, name) -> name.endsWith(".jar"));
            for (File library : libraryFiles != null ? libraryFiles : new File[0]) {
                try {
                    addURL(library.toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        } else {
            getAbstractLogger().info("Loading libraries...");
            File[] libraryFiles = getLibrariesDir().listFiles((dir, name) -> name.endsWith(".jar"));
            for (File library : libraryFiles != null ? libraryFiles : new File[0]) {
                try {
                    addURL(library.toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        getAbstractLogger().info("Current classloader: {}", Thread.currentThread()
                .getContextClassLoader().getClass().getName());
        if (!((Thread.currentThread().getContextClassLoader() instanceof URLClassLoader))) {
            getAbstractLogger().info("Error! URLClassLoader is not a instanceof the current " +
                    "context class loader!");
            System.exit(-1);
        }
        File[] jarFiles = getLuaPluginsDir().listFiles((dir, name) -> name.endsWith(".jar"));
        getAbstractLogger().info("Found {} JAR assets in the plugins directory!", String.valueOf
                (jarFiles != null ? jarFiles.length : 0));
        for (File jarFile : jarFiles != null ? jarFiles : new File[0]) {
            getAbstractLogger().info("Loading {} as a JAR asset...", jarFile.getName());
            try {
                addURL(jarFile.toURI().toURL());
            } catch (MalformedURLException e) {
                getAbstractLogger().info("Java failed on itself.");
                e.printStackTrace();
                System.exit(-1);
            }
        }
        getAbstractLogger().info("Constructing the environment...");
        LuaEnvironment luaEnvironment = new LuaEnvironment();
        getAbstractLogger().info("Initialising the environment's scope...");
        luaEnvironment.initScope();
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
