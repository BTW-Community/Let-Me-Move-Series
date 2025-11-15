package net.dravigen.let_me_see.mixin;

import net.dravigen.let_me_see.LetMeSeeAddon;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin {
	@Shadow public ModelRenderer bipedBody;
	@Shadow public ModelRenderer bipedRightArm;
	@Shadow public ModelRenderer bipedLeftArm;
	@Shadow public ModelRenderer bipedRightLeg;
	@Shadow public ModelRenderer bipedLeftLeg;
	@Shadow public ModelRenderer bipedCloak;
	
	@Shadow public ModelRenderer bipedHead;
	
	@Inject(method = "render", at = @At("HEAD"))
	private void hidePart(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7,
			CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (LetMeSeeAddon.enable3DFirstPerson && entity == mc.thePlayer && mc.gameSettings.thirdPersonView == 0 && !(mc.currentScreen instanceof GuiContainerCreative)) {
			this.bipedBody.showModel = entity.height > 1.4;
			this.bipedHead.showModel = false;
		}
		else {
			this.bipedHead.showModel = true;
			this.bipedBody.showModel = true;
		}
		
		this.bipedCloak.showModel = this.bipedBody.showModel;
	}
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/src/Entity;)V", shift = At.Shift.AFTER))
	private void offsetIf3D(Entity entity, float f, float g, float h, float i, float k, float par7,
			CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		if (LetMeSeeAddon.enable3DFirstPerson &&
				entity == mc.thePlayer &&
				mc.gameSettings.thirdPersonView == 0) {
			float v = 4f;
			this.bipedBody.rotationPointZ += v;
			this.bipedRightArm.rotationPointZ += v;
			this.bipedLeftArm.rotationPointZ += v;
			this.bipedRightLeg.rotationPointZ += v;
			this.bipedLeftLeg.rotationPointZ += v;
			this.bipedCloak.rotationPointZ += v;
			
		}
	}
}
