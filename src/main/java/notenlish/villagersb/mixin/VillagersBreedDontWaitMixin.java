package notenlish.villagersb.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagersBreedDontWaitMixin extends AbstractVillager {

    protected VillagersBreedDontWaitMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract int countFoodPointsInInventory();

    @Shadow
    private int foodLevel;

    // @Shadow
    // public abstract boolean isSleeping();

    // cancellable=True makes it stop the original method
    @Inject(method="canBreed", at=@At("HEAD"), cancellable = true)
    public void injectCanBreed(CallbackInfoReturnable<Boolean> info) {
        // removed the check for this.getAge()
        info.setReturnValue(this.foodLevel + this.countFoodPointsInInventory() >= 12 && !this.isSleeping() && !this.isBaby());
    }
    // I am overriding this
    /*
    * 	in villager.java
	public boolean canBreed() {
		return this.foodLevel + this.countFoodPointsInInventory() >= 12 && !this.isSleeping() && this.getAge() == 0;
	}
    * */

}
