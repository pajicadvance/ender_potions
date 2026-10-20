package me.pajic.ender_potions.platform.neoforge;

//? neoforge {

/*import me.pajic.ender_potions.platform.MultiLoaderUtil;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;

//? >=26.1 {
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
//?}

public class NeoforgeLoaderUtil implements MultiLoaderUtil {

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevEnv() {
        return !FMLLoader/^? >=1.21.9 {^/.getCurrent()/^?}^/.isProduction();
    }

    @Override
    public void s2c(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void c2s(CustomPacketPayload payload) {
        //~ if <26.1 'ClientPacketDistributor' -> 'PacketDistributor'
        ClientPacketDistributor.sendToServer(payload);
    }
}
*///?}
