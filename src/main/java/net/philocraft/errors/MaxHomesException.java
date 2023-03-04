package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import net.philocraft.constants.Colors;

public class MaxHomesException {
    
    private String cause;

    public MaxHomesException() {
        this.cause = Colors.FAILURE.getChatColor() + "You have reached the maximum amount of homes.";
    }

    public MaxHomesException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
