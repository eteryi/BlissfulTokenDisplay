package dev.cross.tokendisplay;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlissfulTokenDisplay extends JavaPlugin {

    private static final long UPDATE_RATE = 20L;

    private BlissfulDisplay display;

    @Override
    public void onEnable() {
        this.display = new BlissfulDisplay();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, display, 0L, UPDATE_RATE);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.display.end();
    }
}
