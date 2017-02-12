package online.pizzacrust.fromage.wrapper.sponge;

import org.slf4j.Logger;

import online.pizzacrust.fromage.common.log.CommonLogger;

/**
 * Implementation of {@link CommonLogger}.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class SLF4JLogger implements CommonLogger {

    private final Logger logger;

    public SLF4JLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

}
