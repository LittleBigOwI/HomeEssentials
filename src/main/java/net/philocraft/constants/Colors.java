package net.philocraft.constants;

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;

public enum Colors {
    
    SUCCESS(Color.decode("#6fed66")),
    FAILURE(Color.decode("#ed6666")),
    WARNING(Color.decode("#f2e055")),
    DETAIL(Color.decode("#66bced")),
    OBFUSCATE(Color.decode("#858585"));

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
