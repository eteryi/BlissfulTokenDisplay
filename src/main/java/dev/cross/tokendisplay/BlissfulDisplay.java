package dev.cross.tokendisplay;

import dev.cross.blissfulcore.api.BlissfulAPI;
import dev.cross.blissfulcore.api.BlissfulTeams;
import dev.cross.blissfulcore.ui.BColors;
import dev.cross.tokendisplay.ui.bossbar.SimpleBossBar;
import dev.cross.tokendisplay.ui.utils.StringFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

public class BlissfulDisplay implements Runnable {

    private final HashMap<Player, SimpleBossBar> bossBars = new HashMap<>();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (bossBars.containsKey(player)) {
                bossBars.get(player).update();
                continue;
            }
            bossBars.put(player, new SimpleBossBar(player, (p) -> {
                BaseComponent component = new TextComponent("\uE210\uE208\uE209");

                int tokens = BlissfulAPI.getImpl().getTokens(p);
                while (tokens > 0) {
                    tokens = tokens / 10;
                    component.addExtra("\uE212");
                }
                tokens = BlissfulAPI.getImpl().getTokens(p);
                component.addExtra(StringFormatter.customString("INDIVIDUAL - "));

                Optional<BlissfulTeams> team = BlissfulAPI.getImpl().getTeamFrom(p);
                if (team.isEmpty()) {
                    return new TextComponent("");
                }

                TextComponent tokenComponent = new TextComponent(tokens + "");
                tokenComponent.setColor(ChatColor.of("#e8d282"));
                tokenComponent.setBold(true);

                component.addExtra(tokenComponent);
                component.addExtra("    ");

                TextComponent teamComponent = new TextComponent(team.get().getTokens() + "");
                teamComponent.setColor(ChatColor.of("#e8d282"));
                teamComponent.setBold(true);


                component.addExtra(teamComponent);
                component.addExtra(" - ");
                component.addExtra(StringFormatter.customString("Team "));

                TextComponent teamDisplay = new TextComponent(StringFormatter.customString(team.get().toString()));
                teamDisplay.setColor(team.get().getColor());
                component.addExtra(teamDisplay);
                BaseComponent offset = new TextComponent("");
                tokens = BlissfulAPI.getImpl().getTokens(p);
                while (tokens > 0) {
                    tokens = tokens / 10;
                    offset.addExtra("\uE211");
                }

                String teamName = team.get().toString();
                for (int i = 4; i < teamName.length(); i++) {
                    offset.addExtra("\uE211");
                }

                offset.addExtra(component);
                return offset;
            }));
            bossBars.get(player).start();
        }
    }

    public void end() {
        for (Player p : this.bossBars.keySet()) {
            this.bossBars.get(p).kill();
        }
    }
}
