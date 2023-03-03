package net.philocraft.components;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.philocraft.constants.Colors;

public class WarningComponent {
    
    private Player player;
    private String[] message;
    private String acceptCommand;
    private String denyCommand;

    private final String YES = ChatColor.translateAlternateColorCodes('&', "&6[&a✔&6] ");
    private final String NO = ChatColor.translateAlternateColorCodes('&', "&6[&c✘&6]");

    public WarningComponent(Player player, String[] message, String acceptCommand, String denyCommand) {
        this.player = player;
        this.message = message;
        this.acceptCommand = acceptCommand;
        this.denyCommand = denyCommand;
    }

    private BaseComponent[] build() {
        
        TextComponent[] message = new TextComponent[this.message.length];
        for(int i = 0; i < this.message.length; i++) {
            message[i] = new TextComponent(this.message[i]);
            if(i%2 == 0) {
                message[i].setColor(Colors.WARNING.getChatColor());
            } else {
                message[i].setColor(Colors.COMMON.getChatColor());
            }
        }

        TextComponent accept = new TextComponent(YES);
        TextComponent deny = new TextComponent(NO);

        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("confirm")));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("cancel")));

        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.acceptCommand));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.denyCommand));

        ComponentBuilder finalMessage = new ComponentBuilder();
        for(TextComponent t : message) {
            finalMessage.append(t);
        }

        finalMessage.append(accept);
        finalMessage.append(deny);

        return finalMessage.create();
    }

    public void send() {
        this.player.spigot().sendMessage(this.build());
    }
}
