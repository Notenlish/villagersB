package notenlish.villagersb;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class VillagersbParticles {

    public static void ExplosionParticles(ServerLevel level, Vec3 start_pos, double speedMax, int count) {
        RandomSource random = level.getRandom();

        // level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, this.getX(), this.getY() + 2, this.getZ(), 100, 0.3, 0.8, 0.3, 0.1);

        for (int i = 0; i < count; i++) {
            var angle = random.nextDouble() * Math.PI * 2;
            var speed = random.nextDouble() * 0.1 + speedMax;

            var spread_x = 0.1;
            var spread_y = 0.2;
            var spread_z = 0.1;

            level.sendParticles(
                    ParticleTypes.TOTEM_OF_UNDYING, start_pos.x, start_pos.y, start_pos.z, 1, spread_x, spread_y, spread_z, speed
            );
        }
    }
}
