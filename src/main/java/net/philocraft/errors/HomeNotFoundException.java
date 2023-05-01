package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class HomeNotFoundException {
    
    private String cause;

    public HomeNotFoundException() {
        this.cause = Colors.FAILURE.getChatColor() + "Couldn't find a matching home.";
    }

    public HomeNotFoundException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
