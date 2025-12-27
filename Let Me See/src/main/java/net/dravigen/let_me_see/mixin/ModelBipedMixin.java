package net.dravigen.let_me_see.mixin;

import api.AddonHandler;
import btw.entity.model.PlayerArmorModel;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.let_me_see.LetMeSeeAddon;
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
	
	@Shadow
	public ModelRenderer bipedRightLeg;
	
	@Shadow
	public ModelRenderer bipedLeftLeg;
	
	@Inject(method = "render", at = @At("HEAD"))
	private void hideBasePart(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7,
			CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean isArmor = (Object) this instanceof PlayerArmorModel;
		
		if (!isArmor){
			this.bipedHead.showModel = true;
			this.bipedHeadwear.showModel = true;
			this.bipedBody.showModel = true;
			this.bipedRightArm.showModel = true;
			this.bipedLeftArm.showModel = true;
			this.bipedRightLeg.showModel = true;
			this.bipedLeftLeg.showModel = true;
		}
		
		if (entity != mc.thePlayer) return;
		
		boolean isFirstPers = LetMeSeeAddon.isIsCustomFirstPerson(mc);
		
		if (isFirstPers) {
			this.bipedHead.showModel = false;
			this.bipedHeadwear.showModel = false;
		}
		
		ItemStack helmet = mc.thePlayer.getCurrentArmor(3);
		boolean hasHelmet = helmet != null && helmet.getItem() instanceof ItemArmor;
		ItemStack chestplate = mc.thePlayer.getCurrentArmor(2);
		boolean hasChest = chestplate != null && chestplate.getItem() instanceof ItemArmor ;
		
		if (isFirstPers) {
			boolean isSliding = false;
			
			if (AddonHandler.isModInstalled("let_me_move_ex")) {
				isSliding = ((ICustomMovementEntity) entity).lmm_$isAnimation(new ResourceLocation("LMM",
																								   "wallSliding"));
			}
			
			boolean showChest = mc.thePlayer.isPlayerSleeping() || entity.height > 1.4 && !isSliding;
			if (isArmor) {
				this.bipedBody.showModel = hasChest && (showChest) && this.bipedBody.showModel;
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
					this.bipedRightArm.showModel = hasChest && this.bipedRightArm.showModel;
					this.bipedLeftArm.showModel = hasChest && this.bipedLeftArm.showModel;
				}
				else {
					this.bipedRightArm.showModel = true;
					this.bipedLeftArm.showModel = true;
				}
			}
		}
		else {
			if (isArmor) {
				this.bipedHead.showModel = hasHelmet && this.bipedHead.showModel;
				this.bipedHeadwear.showModel = hasHelmet && this.bipedHeadwear.showModel;
				this.bipedBody.showModel = hasChest && this.bipedBody.showModel;
				this.bipedRightArm.showModel = hasChest && this.bipedRightArm.showModel;
				this.bipedLeftArm.showModel = hasChest && this.bipedLeftArm.showModel;
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
