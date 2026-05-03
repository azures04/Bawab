package fr.azures.bawab;

public class Constants {

    public static final String MODID = "bawab";
    public static final String CHANNEL = "bawab:auth";
    public static String MINECRAFT_SERVICES = "https://api.minecraftservices.com";
    public static String SESSION_SERVER = "https://sessionserver.mojang.com/";

    public static void changeMinecraftServicesProvider(String url) {
        MINECRAFT_SERVICES = url;
    }

    public static void changeMinecraftSessionProvider(String url) {
        SESSION_SERVER = url;
    }
}
