package notenlish.villagersb;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Villagersb implements ModInitializer {
    public static final String MOD_ID = "villagersb";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("AHAHAHAHAH");

        LOGGER.info("Hello Fabric world! This is VILLAGERSB mod talking!!!!!!");


        DoStuff();
    }

    private void DoStuff() {
        // register the payload client -> server
        PayloadTypeRegistry.serverboundPlay().register(ServerBoundVillagersbCarryPayload.TYPE, ServerBoundVillagersbCarryPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ServerBoundVillagersbCarryPayload.TYPE, (payload, context) -> {
                Entity entity = context.player().level().getEntity(payload.entityId());

                if (entity instanceof Villager && entity instanceof LivingEntity ) {
                    entity.save()


                    entity.discard();  // remove without loot appearing
                }

                // if (entity instanceof LivingEntity livingEntity && livingEntity.closerThan(context.player(), 5)) {
                //    livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100));
                // }
        })
    }
}