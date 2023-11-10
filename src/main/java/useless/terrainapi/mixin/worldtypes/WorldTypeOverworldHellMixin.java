package useless.terrainapi.mixin.worldtypes;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.type.WorldTypeOverworldHell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.terrainapi.generation.hell.api.ChunkGeneratorOverworldHellAPI;
@Mixin(value = WorldTypeOverworldHell.class, remap = false)
public class WorldTypeOverworldHellMixin {
	@Inject(method = "createChunkGenerator(Lnet/minecraft/core/world/World;)Lnet/minecraft/core/world/generate/chunk/ChunkGenerator;", at = @At("HEAD"), cancellable = true)
	private void customChunkGenerator(World world, CallbackInfoReturnable<ChunkGenerator> cir){
		cir.setReturnValue(new ChunkGeneratorOverworldHellAPI(world));
	}
}
