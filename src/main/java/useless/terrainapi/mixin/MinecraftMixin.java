package useless.terrainapi.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.terrainapi.TerrainMain;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
	@Inject(method = "startGame()V", at = @At("HEAD"))
	private void initializeGeneration(CallbackInfo ci){
		TerrainMain.loadModules();
	}
}
