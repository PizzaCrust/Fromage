package online.pizzacrust.fromage.common.env;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import online.pizzacrust.fromage.common.LuaObject;

/**
 * Represents the annotated element as a FromageLib.
 * The annotated element must extend {@link LuaObject.Static}.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromageLib {
}
