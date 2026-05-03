package fr.azures.bawab.network.payloads;

import fr.azures.bawab.Constants;
import fr.azures.bawab.configuration.BawabConfig;
import fr.azures.bawab.services.Commands;
import fr.azures.bawab.services.Yggdrasil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AuthProcess() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AuthProcess> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "authprocess"));

    public static final StreamCodec<ByteBuf, AuthProcess> STREAM_CODEC = StreamCodec.unit(
        new AuthProcess()
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final AuthProcess payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            String username = context.player().getName().getString();
            if (Yggdrasil.serverIds.containsKey(username)) {
                try {
                    boolean hasJoined = Yggdrasil.hasJoined(username, Yggdrasil.serverIds.get(username));
                    if (hasJoined) {
                        String commandTemplate = BawabConfig.AUTH_COMMAND.get();
                        String finalCommand = String.format(commandTemplate, username);
                        Commands.execute(context.player().getServer(), finalCommand);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
