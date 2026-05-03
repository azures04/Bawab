package fr.azures.bawab.network.payloads;

import fr.azures.bawab.Constants;
import fr.azures.bawab.services.Yggdrasil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.concurrent.CompletableFuture;

public record SendServerId(String serverId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SendServerId> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "sendserverid"));

    public static final StreamCodec<ByteBuf, SendServerId> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SendServerId::serverId,
            SendServerId::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SendServerId payload, final IPayloadContext context) {
        CompletableFuture.runAsync(() -> {
            try {
                var user = Minecraft.getInstance().getUser();
                Yggdrasil.joinServer(user.getAccessToken(), user.getProfileId(), payload.serverId());
                PacketDistributor.sendToServer(new AuthProcess());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}