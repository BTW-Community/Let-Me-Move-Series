package net.dravigen.let_me_see.mixin;

import api.AddonHandler;
import btw.entity.model.PlayerArmorModel;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.let_me_see.config.LMS_Settings;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin {
	@Shadow
	public ModelRenderer bipedBody;
	@Shadow
	public ModelRenderer bipedRightArm;
	@Shadow
	public ModelRenderer bipedLeftArm;
	@Shadow
	public ModelRenderer bipedCloak;
	@Shadow
	public ModelRenderer bipedHead;
	@Shadow
	public ModelRenderer bipedHeadwear;
	
	@Inject(method = "render", at = @At("HEAD"))
	private void hideBasePart(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7,
			CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean isFirstPers = LMS_Settings.FIRST_PERSON_MODEL.getBool() &&
				entity == mc.thePlayer &&
				mc.gameSettings.thirdPersonView == 0 &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory);
		
		if (isFirstPers) {
			this.bipedHead.showModel = false;
			this.bipedHeadwear.showModel = false;
		}
		
		boolean isArmor = (Object) this instanceof PlayerArmorModel;
		boolean hasHelmet = mc.thePlayer.getCurrentArmor(3) != null;
		boolean hasChest = mc.thePlayer.getCurrentArmor(2) != null;
		
		if (isFirstPers) {
			boolean isSliding = false;
			
			if (AddonHandler.isModInstalled("let_me_move")) {
				isSliding = ((ICustomMovementEntity) entity).lmm_$isAnimation(new ResourceLocation("LMM",
																								   "wallSliding"));
			}
			
			boolean showChest = mc.thePlayer.isPlayerSleeping() || entity.height > 1.4 && !isSliding;
			if (isArmor) {
				this.bipedBody.showModel = hasChest && (showChest);
			}
			else {
				this.bipedBody.showModel = showChest;
			}
			
			ItemStack heldItem = mc.thePlayer.getHeldItem();
			
			if (heldItem != null && (heldItem.itemID == Item.map.itemID)) {
				this.bipedRightArm.showModel = false;
				this.bipedLeftArm.showModel = false;
			}
			else {
				if (isArmor) {
					this.bipedRightArm.showModel = hasChest;
					this.bipedLeftArm.showModel = hasChest;
				}
				else {
					this.bipedRightArm.showModel = true;
					this.bipedLeftArm.showModel = true;
				}
			}
		}
		else {
			if (isArmor) {
				this.bipedHead.showModel = hasHelmet;
				this.bipedHeadwear.showModel = hasHelmet;
				this.bipedBody.showModel = hasChest;
				this.bipedRightArm.showModel = hasChest;
				this.bipedLeftArm.showModel = hasChest;
			}
			else {
				this.bipedHead.showModel = true;
				this.bipedHeadwear.showModel = true;
				this.bipedBody.showModel = true;
				this.bipedRightArm.showModel = true;
				this.bipedLeftArm.showModel = true;
			}
		}
		
		this.bipedCloak.showModel = this.bipedBody.showModel;
	}
}
