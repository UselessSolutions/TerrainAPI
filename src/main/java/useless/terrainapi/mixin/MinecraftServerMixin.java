package useless.terrainapi.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.terrainapi.TerrainMain;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixin {
	@Inject(method = "startServer()Z", at = @At("HEAD"))
	private void initializeGeneration(CallbackInfoReturnable<Boolean> cir){
		TerrainMain.loadModules();
	}
}
