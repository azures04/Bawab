package fr.azures.bawab.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BawabConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> KICK_MESSAGE = BUILDER
        .comment("Le message affiché au joueur lors d'un kick.")
        .define("kick_message", "Quelque chose ne va pas avec votre connexion.");

    public static final ModConfigSpec.ConfigValue<String> AUTH_COMMAND = BUILDER
        .comment("La commande exécutée lors d'une auth réussie. Utilisez %s pour le pseudo.")
        .define("auth_command", "authme forcelogin %s");

    public static final ModConfigSpec SPEC = BUILDER.build();

}
