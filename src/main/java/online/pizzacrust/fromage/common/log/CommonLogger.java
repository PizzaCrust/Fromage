package online.pizzacrust.fromage.common.log;

import online.pizzacrust.fromage.BasePlugin;

/**
 * Represents a common logger for different logging implementations.
 * Used for example, a class; {@link BasePlugin}.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public interface CommonLogger {

    void info(String msg);

    default void info(String msg, String... replaces) {
        String replaced = msg.replace("{}", "%s");
        info(String.format(replaced, (Object[]) replaces));
    }

}
