package fr.azures.bawab.registries;

import fr.azures.bawab.network.NetworkSetup;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Consumer;

public class NetworkRegistry {

    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0").optional();

        for (Consumer<PayloadRegistrar> packetRegistration : NetworkSetup.PACKETS.values()) {
            packetRegistration.accept(registrar);
        }
    }

}
