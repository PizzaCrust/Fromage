package online.pizzacrust.fromage.sponge;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

import online.pizzacrust.fromage.api.Fromage;
import online.pizzacrust.fromage.api.LuaPlugin;

public class SCommandLuaPlugins implements CommandExecutor {

    private Optional<LuaPlugin> getPlugin(String name) {
        for (LuaPlugin plugin : Fromage.PLUGINS) {
            if (plugin.getName().equals(name)) {
                return Optional.of(plugin);
            }
        }
        return Optional.empty();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> pluginName = args.getOne("pluginName");
        if (pluginName.isPresent()) {
            Optional<LuaPlugin> luaPluginOpt = getPlugin(pluginName.get());
            if (!luaPluginOpt.isPresent()) {
                Text error = Text.builder("Sorry, the plugin (" + pluginName.get() + ") doesn't " +
                        "exist.").color(TextColors.RED).build();
                src.sendMessage(error);
                return CommandResult.success();
            }
            LuaPlugin luaPlugin = luaPluginOpt.get();
            src.sendMessage(Text.builder("Name: ").color(TextColors.YELLOW).append(Text.of
                    (luaPlugin.getName()))
                    .build
                    ());
            src.sendMessage(Text.builder("Description: ").color(TextColors.YELLOW).append(Text
                    .of(luaPlugin.getDescription())).build());
            src.sendMessage(Text.builder("Version: ").color(TextColors.YELLOW).append(Text.of
                    (String.valueOf(luaPlugin.getVersion()))).build());
            return CommandResult.success();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            final int[] index = {1};
            Fromage.PLUGINS.forEach((luaPlugin) -> {
                if (index[0] == Fromage.PLUGINS.size() && Fromage.PLUGINS.size() != 0) {
                    stringBuilder.append("and ").append(luaPlugin.getName()).append(".");
                } else {
                    stringBuilder.append(luaPlugin.getName()).append(", ");
                }
                index[0]++;
            });
            Text plugins = Text.builder("Plugins: ").append(Text.of(stringBuilder.toString())).build();
            src.sendMessage(plugins);
        }
        return CommandResult.success();
    }
}
