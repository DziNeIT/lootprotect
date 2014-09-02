package pw.ollie.lootprotect;

import org.bukkit.plugin.java.JavaPlugin;

public final class LootProtect extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new LPListener(this), this);
    }

    @Override
    public void onDisable() {
    }
}
