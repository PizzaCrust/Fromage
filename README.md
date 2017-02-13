# Fromage
Fromage allows developers to quickly create Sponge plugins quickly using the Lua scripting language.

### Features

- Easy deployment into Sponge, simply place the script inside the configuration directory.
- Easy language to learn, API names directly use Java names; so no more naming confusion!
- Fromage is very extensible and has a low-level API to access unsupported items.

### Examples

- Displays "hello world" at load, and "alive" on enable, and also "ded" on disable.
```lua
broadcast("hello world")
newPlugin("HelloWorld", "This is a hello world plugin", 0.1, function (plugin)
    plugin.onEnable(function ()
        broadcast("alive")
    end)
    plugin.onDisable(function ()
        broadcast("ded")
    end)
end)
```