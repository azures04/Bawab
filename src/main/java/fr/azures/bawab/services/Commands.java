package fr.azures.bawab.services;

import net.minecraft.server.MinecraftServer;

public class Commands {

    public static void execute(MinecraftServer server, String command) {
        if (server != null) {
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
        }
    }

}
