package net.dravigen.let_me_see.mixin;

import net.dravigen.let_me_see.LetMeSeeAddon;
import net.minecraft.src.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {
	
	@Shadow
	private Minecraft mc;
	
	@Redirect(method = "renderEntities", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;thirdPersonView:I", opcode = Opcodes.GETFIELD, ordinal = 0))
	private int test(GameSettings instance) {
		if (LetMeSeeAddon.enable3DFirstPerson && !(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory)) {
			return 1;
		}
		
		return instance.thirdPersonView;
	}
}
