package notenlish.villagersb;

import com.mojang.brigadier.Message;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Villagersb implements ModInitializer {
    public static final String MOD_ID = "villagersb";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final AttachmentType<Boolean> PLAYER_HAS_BABY_VILLAGER_ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("villagers", "player_has_baby_villager_attachment"),
            booleanBuilder -> booleanBuilder.initializer(() -> false)
                    .syncWith(
                            ByteBufCodecs.BOOL,
                            AttachmentSyncPredicate.all()
                    )
    );
    public static final AttachmentType<Boolean> PLAYER_HAS_VILLAGER_ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("villagersb","player_has_villager_attachment"),
            booleanBuilder -> booleanBuilder.initializer(() -> false)
                    .syncWith(
                        ByteBufCodecs.BOOL,
                        // sync data to all
                        AttachmentSyncPredicate.all()
                    )
    );

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("AHAHAHAHAH");

        LOGGER.info("Hello Fabric world! This is VILLAGERSB mod talking!!!!!!");


        DoStuff();
    }

    private void SendMsgToChatEveryone(MinecraftServer server, Component msg) {
        server.getPlayerList().broadcastSystemMessage(msg, false); // false is for chat
    }

    private void SpawnVillagerNearPlayer(MinecraftServer server, ServerPlayer player, boolean is_baby) {
        var player_pos = player.position();
        var look = player.getLookAngle();
        var flatLook = new Vec3(look.x, 0, look.z);

        SendMsgToChatEveryone(server, Component.literal("Player look angle " + look));

        Villager villager = EntityType.VILLAGER.create(
                server.getLevel(player.level().dimension()),
                null,
                new BlockPos(0,0,0),
                EntitySpawnReason.NATURAL,
                false,
                false
        );


        if (villager != null) {
            villager.setBaby(is_baby);
            // move to infront of the player
            villager.setPos(player_pos.add(flatLook.scale(1.5)).scale(1.0));
            player.level().addFreshEntity(villager);
        }
    }

    private void DoStuff() {
        // register the payload client -> server
        PayloadTypeRegistry.serverboundPlay().register(ServerBoundVillagersbCarryPayload.TYPE, ServerBoundVillagersbCarryPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ServerBoundVillagersbCarryPayload.TYPE, (payload, context) -> {
                Boolean player_has_villager = context.player().getAttachedOrSet(PLAYER_HAS_VILLAGER_ATTACHMENT, false);
                Boolean player_has_baby = context.player().getAttachedOrSet(PLAYER_HAS_BABY_VILLAGER_ATTACHMENT, false);

                int ent_id = payload.entityId();
                if (ent_id != -1) {
                    Entity entity = context.player().level().getEntity(payload.entityId());
                    if (entity instanceof Villager) {
                        if (player_has_villager) {
                            // stop carrying villager
                            context.player().setAttached(PLAYER_HAS_VILLAGER_ATTACHMENT, false);
                            LOGGER.info("Set attachment to false");

                            SpawnVillagerNearPlayer(context.server(), context.player(), player_has_baby);

                        } else {
                            context.player().setAttached(PLAYER_HAS_VILLAGER_ATTACHMENT, true);
                            LOGGER.info("Set attachment to true");
                            context.player().setAttached(PLAYER_HAS_BABY_VILLAGER_ATTACHMENT, ((Villager) entity).isBaby());
                            entity.discard();  // remove without loot appearing
                        }
                    }
                } else {
                    // just drop the villager if player is carrying it
                    if (player_has_villager) {
                        context.player().setAttached(PLAYER_HAS_VILLAGER_ATTACHMENT, false);
                        LOGGER.info("Set attachment to false");

                        SpawnVillagerNearPlayer(context.server(), context.player(), player_has_baby);
                    }
                }


                // if (entity instanceof LivingEntity livingEntity && livingEntity.closerThan(context.player(), 5)) {
                //    livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100));
                // }
        });
    }
}