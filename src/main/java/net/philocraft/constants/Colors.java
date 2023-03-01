package net.philocraft.constants;

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;

public enum Colors {
    
    SUCCESS(Color.decode("#77dd77")),
    FAILURE(Color.decode("#ff6961")),
    WARNING(Color.decode("#ddb877"));

    private Color color;

    private Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public ChatColor getChatColor() {
        return ChatColor.of(this.color);
    }
}
