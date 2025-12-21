package net.dravigen.let_me_move.mixin.client.render;

import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.Render;
import net.minecraft.src.RendererLivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin extends Render {
	@Redirect(method = "doRenderLiving", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityLivingBase;hurtTime:I", opcode = Opcodes.GETFIELD, ordinal = 1))
	private int noRedGlintOnHurt(EntityLivingBase instance) {
		return 0;
	}
}
