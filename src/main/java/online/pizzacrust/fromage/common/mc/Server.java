package online.pizzacrust.fromage.common.mc;

/**
 * Represents a server, this class is used for common code between server APIs.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public interface Server {

    /**
     * The server's instance. Access the server's instance by the array's first index.
     */
    Server[] INSTANCE = new Server[1];

    /**
     * Broadcasts a message to every player.
     * @param msg
     */
    void broadcast(String msg);

}
