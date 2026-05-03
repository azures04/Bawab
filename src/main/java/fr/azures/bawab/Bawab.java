package fr.azures.bawab;

import fr.azures.bawab.configuration.BawabConfig;
import fr.azures.bawab.registries.NetworkRegistry;
import fr.azures.bawab.services.Yggdrasil;
import fr.azures.bawab.listeners.PlayerEvents;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Constants.MODID)
public class Bawab {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static Yggdrasil authService;

    public Bawab(IEventBus modEventBus, ModContainer modContainer) throws Exception {
        modContainer.registerConfig(ModConfig.Type.SERVER, BawabConfig.SPEC);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(NetworkRegistry::registerPackets);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new PlayerEvents());

        authService = new Yggdrasil();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
