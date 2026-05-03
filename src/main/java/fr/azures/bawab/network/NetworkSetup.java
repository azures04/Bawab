package fr.azures.bawab.network;

import fr.azures.bawab.network.payloads.AuthProcess;
import fr.azures.bawab.network.payloads.SendServerId;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NetworkSetup {

    public static final Map<String, Consumer<PayloadRegistrar>> PACKETS = new HashMap<>();

    static {
        PACKETS.put("sendserverid", registrar -> registrar.playBidirectional(
            SendServerId.TYPE,
            SendServerId.STREAM_CODEC,
            SendServerId::handle
        ));
        PACKETS.put("authprocess", registrar -> registrar.playBidirectional(
            AuthProcess.TYPE,
            AuthProcess.STREAM_CODEC,
            AuthProcess::handle
        ));
    }

}

