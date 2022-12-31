package de.neo.signfinder.commands;

import de.neo.signfinder.sign.SignInfo;
import de.neo.signfinder.sql.SignDB;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.TextSerializer;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SignFinderCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /signfinder <search>");
            return true;
        }
        if (!sender.hasPermission("signfinder.search")) {
            sender.sendMessage(ChatColor.RED + "You don't have the permission to do this.");
            return true;
        }
        StringBuilder expression = new StringBuilder();
        for (String arg : args) {
            expression.append(arg).append(" ");
        }
        List<SignInfo> signs = SignDB.getSignsWithText("%" + expression.toString().trim() + "%");
        for (SignInfo sign : signs) {
            List<BaseComponent> components =
                    new ArrayList<>(List.of(TextComponent.fromLegacyText(ChatColor.GRAY + "[" + ChatColor.GREEN + sign.getLocation().getBlockX()
                    + ChatColor.GRAY + "," + ChatColor.GREEN + sign.getLocation().getBlockY() + ChatColor.GRAY + "," + ChatColor.GREEN
                    + sign.getLocation().getBlockZ() + ChatColor.GRAY + "] " + ChatColor.RESET)));
            for (String line : sign.getLines()) {
                components.addAll(List.of(ComponentSerializer.parse(line)));
                if (!line.equals(sign.getLines().get(sign.getLines().size() - 1))) {
                    components.add(new TextComponent(", "));
                }
            }
            // Add click and hover event
            for (BaseComponent component : components) {
                if (component == null) continue;
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        "/tp " + sign.getLocation().getBlockX() + " " + sign.getLocation().getBlockY()
                                + " " + sign.getLocation().getBlockZ()));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new BaseComponent[] { new TextComponent(ChatColor.GRAY + "Click to teleport to this sign.") }));
            }
            sender.spigot().sendMessage(components.stream().filter(Objects::nonNull).toArray(BaseComponent[]::new));
        }
        if (signs.size() == 0) {
            sender.sendMessage(ChatColor.RED + "No signs found.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
