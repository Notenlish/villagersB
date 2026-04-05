package notenlish.villagersb.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import notenlish.villagersb.ServerBoundVillagersbCarryPayload;
import notenlish.villagersb.Villagersb;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class VillagersbClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		KeyMapping.Category VILLAGERSB_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(Villagersb.MOD_ID, "villagersb"));
		KeyMapping PickUpVillagerKeyMapping = KeyMappingHelper.registerKeyMapping(
				new KeyMapping(
						"key.villagersb.pickup_villager",  // translation key
						InputConstants.Type.KEYSYM,
						GLFW.GLFW_KEY_V,
						VILLAGERSB_CATEGORY
				)
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (PickUpVillagerKeyMapping.consumeClick()) {
				if (client.player != null && client.level != null) {

					double distance = 5.0;
					List<Villager> villagers_list = client.level.getEntitiesOfClass(Villager.class, client.player.getBoundingBox().inflate(10), o -> o.distanceTo(client.player) < distance);
					// client.player.sendSystemMessage(Component.literal(String.format("villagers: " + villagers_list)));

					float closest = 99999;
					int idOfClosestVillager = -1;
					for (Villager villager: villagers_list) {
						float distanceToPlayer = villager.distanceTo(client.player);
						if (distanceToPlayer < closest ) {
							closest = distanceToPlayer;
							idOfClosestVillager = villager.getId();
						}
					}

					if (idOfClosestVillager != -1) {
						// client.player.sendSystemMessage(Component.literal("ID of closest villager: " + idOfClosestVillager));

						ServerBoundVillagersbCarryPayload payload = new ServerBoundVillagersbCarryPayload(idOfClosestVillager);
						ClientPlayNetworking.send(payload);
					} else {
						ServerBoundVillagersbCarryPayload payload = new ServerBoundVillagersbCarryPayload(idOfClosestVillager);
						ClientPlayNetworking.send(payload);
					}
				}
			}
		});

	}
}