package notenlish.villagersb.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.level.Level;
import notenlish.villagersb.Villagersb;
import notenlish.villagersb.VillagersbParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class DontBeLazyIfBribedMixin extends AbstractVillager {

	@Shadow
	public abstract VillagerData getVillagerData();

	@Shadow
	public abstract void setVillagerData(VillagerData data);

	protected DontBeLazyIfBribedMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

	// possible effects to use: dust_plume, portal, enchant, cloud

	@Inject(method = "pickUpItem", at=@At("HEAD"), cancellable = false)
	protected void pickUpItem(ServerLevel level, ItemEntity entity, CallbackInfo ci) {
		// Villagersb.LOGGER.info("Entity type of the item villager is picking up: " + entity.getItem());

		var item_stack = entity.getItem();
		var villager_data = this.getVillagerData();
		if (item_stack.is(Items.EMERALD) && villager_data.profession().is(VillagerProfession.NITWIT)) {

			HolderGetter.Provider registries = level.registryAccess();
			var new_data = villager_data.withProfession(registries, VillagerProfession.NONE);
			this.setVillagerData(new_data);

			var particle_start_pos = this.position().add(0,1.5,0);
			VillagersbParticles.ExplosionParticles(level, particle_start_pos, 0.4, 150);
		}
	}

    /*
    * 	@Override
	*   protected void pickUpItem(final ServerLevel level, final ItemEntity entity) {
	*	    InventoryCarrier.pickUpItem(level, this, this, entity);
	*   }
    * */
}
