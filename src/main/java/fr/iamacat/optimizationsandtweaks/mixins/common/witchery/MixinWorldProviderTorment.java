package fr.iamacat.optimizationsandtweaks.mixins.common.witchery;

import com.emoniph.witchery.dimension.WorldChunkManagerTorment;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import fr.iamacat.optimizationsandtweaks.utilsformods.witchery.WorldChunkManagerTorment2;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldProviderTorment.class)
public abstract class MixinWorldProviderTorment extends WorldProvider {
 @Overwrite(remap = false)
 public IChunkProvider func_76555_c() {
     return new WorldChunkManagerTorment2(this.worldObj);
 }
}
