package fr.azures.bawab.listeners;

import fr.azures.bawab.configuration.BawabConfig;
import fr.azures.bawab.network.payloads.SendServerId;
import fr.azures.bawab.services.Yggdrasil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.crypto.SecretKey;

public class PlayerEvents {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer serverPlayer = (ServerPlayer) event.getEntity();
        if (!serverPlayer.connection.hasChannel(SendServerId.TYPE)) {
            return;
        }
        String playerName = event.getEntity().getName().getString();
        if (!Yggdrasil.serverIds.containsKey(playerName)) {
            try {
                SecretKey sharedScecret = Yggdrasil.generateSharedSecret();
                String serverId = Yggdrasil.generateServerId("", Yggdrasil.MINECRAFT_PUBLIC_KEY, sharedScecret);
                Yggdrasil.serverIds.put(playerName, serverId);
                PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new SendServerId(serverId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            serverPlayer.connection.disconnect(Component.literal(BawabConfig.KICK_MESSAGE.get()));
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        String playerName = event.getEntity().getName().getString();
        if (Yggdrasil.serverIds.containsKey(playerName)) {
            Yggdrasil.serverIds.remove(playerName);
        }
    }

}
