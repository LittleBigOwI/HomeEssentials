package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import net.philocraft.constants.Colors;

public class InvalidArgumentsException {
    
    private String cause;

    public InvalidArgumentsException() {
        this.cause = Colors.FAILURE.getChatColor() + "Invalid arguments.";
    }

    public InvalidArgumentsException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
