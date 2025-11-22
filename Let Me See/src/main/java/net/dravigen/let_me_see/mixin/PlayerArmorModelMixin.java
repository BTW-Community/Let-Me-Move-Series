package net.dravigen.let_me_see.mixin;

import btw.entity.model.PlayerArmorModel;
import net.dravigen.dranimation_lib.DraNimationLibAddon;
import net.dravigen.let_me_see.config.LmsSettings;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerArmorModel.class)
public abstract class PlayerArmorModelMixin extends ModelBiped {
	@Inject(method = "render", at = @At("HEAD"))
	private void hideArmorPart(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7,
			CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (DraNimationLibAddon.settingsManager.getBoolean(LmsSettings.firstPersonModelID) &&
				entity == mc.thePlayer &&
				mc.gameSettings.thirdPersonView == 0 &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory)) {
			this.bipedBody.showModel = entity.height > 1.4;
			this.bipedHead.showModel = false;
			
			ItemStack heldItem = mc.thePlayer.getHeldItem();
			if (heldItem != null && (heldItem.itemID == Item.map.itemID)) {
				this.bipedRightArm.showModel = false;
				this.bipedLeftArm.showModel = false;
			}
			else {
				this.bipedRightArm.showModel = true;
				this.bipedLeftArm.showModel = true;
			}
		}
		else {
			this.bipedHead.showModel = true;
			this.bipedBody.showModel = true;
			this.bipedRightArm.showModel = true;
			this.bipedLeftArm.showModel = true;
		}
		
		this.bipedCloak.showModel = this.bipedBody.showModel;
	}
}
