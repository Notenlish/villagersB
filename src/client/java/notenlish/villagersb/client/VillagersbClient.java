package notenlish.villagersb.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import notenlish.villagersb.Villagersb;
import org.lwjgl.glfw.GLFW;

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
				if (client.player != null) {
					client.player.sendSystemMessage(Component.literal("Key pressed!!!"));
				}
			}
		});

	}
}