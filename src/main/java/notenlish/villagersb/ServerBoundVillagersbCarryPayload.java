package notenlish.villagersb;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;


// client will do the raycast, sends entityId
public record ServerBoundVillagersbCarryPayload(int entityId) implements CustomPacketPayload {
    public static final Identifier CARRY_VILLAGER_PAYLOAD_ID = Identifier.fromNamespaceAndPath(Villagersb.MOD_ID, "carry_villager");
    public static final CustomPacketPayload.Type<ServerBoundVillagersbCarryPayload> TYPE = new CustomPacketPayload.Type<>(CARRY_VILLAGER_PAYLOAD_ID);

    // this is so that the game knows how to serialize/deserialize the data.
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBoundVillagersbCarryPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, ServerBoundVillagersbCarryPayload::entityId, ServerBoundVillagersbCarryPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
