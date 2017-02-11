package online.pizzacrust.fromage.wrapper;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import online.pizzacrust.fromage.common.mc.Server;

/**
 * Implementation of {@link Server} for Sponge.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class SpongeServer implements Server {
    @Override
    public void broadcast(String msg) {
        Sponge.getGame().getServer().getOnlinePlayers().forEach((player) -> player.sendMessage(Text.of(msg)));
    }
}
