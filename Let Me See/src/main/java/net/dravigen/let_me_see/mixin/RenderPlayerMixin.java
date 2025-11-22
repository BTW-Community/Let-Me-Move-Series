package net.dravigen.let_me_see.mixin;

import btw.client.texture.CustomUpdatingTexture;
import net.dravigen.dranimation_lib.DraNimationLibAddon;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.let_me_see.LetMeSeeAddon;
import net.dravigen.let_me_see.config.LmsSettings;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin extends RendererLivingEntity {
	
	@Shadow
	private ModelBiped modelBipedMain;
	
	public RenderPlayerMixin(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemRenderer;renderItem(Lnet/minecraft/src/EntityLivingBase;Lnet/minecraft/src/ItemStack;I)V", ordinal = 2))
	private void renderCompassAndClock(ItemRenderer instance, EntityLivingBase var15, ItemStack var16, int var5) {
		ItemStack var8 = var15.getHeldItem();
		Icon icon = var8.getItem().getIconFromDamage(0);
		if (icon instanceof CustomUpdatingTexture customUpdateTexture) {
			customUpdateTexture.updateActive(CustomUpdatingTexture.FIRST_PERSON);
		}
		instance.renderItem(var15, var16, var5);
		
		icon = var8.getItem().getIconFromDamage(0);
		if (icon instanceof CustomUpdatingTexture customUpdateTexture) {
			customUpdateTexture.updateInert(CustomUpdatingTexture.FIRST_PERSON);
		}
	}
	
	@Inject(method = "renderFirstPersonArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;render(F)V"))
	private void show1stPersonArmIfMap(EntityPlayer player, CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack heldItem = mc.thePlayer.getHeldItem();
		
		if (DraNimationLibAddon.settingsManager.getBoolean(LmsSettings.firstPersonModelID) &&
				mc.gameSettings.thirdPersonView == 0 &&
				heldItem != null &&
				(heldItem.itemID == Item.map.itemID) &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory)) {
			this.modelBipedMain.bipedRightArm.showModel = true;
			this.modelBipedMain.bipedLeftArm.showModel = true;
		}
	}
	
	@Inject(method = "rotatePlayer", at = @At("HEAD"))
	private void offsetPlayerIn1stPerson(AbstractClientPlayer entity, float par2, float par3, float par4,
			CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		if (DraNimationLibAddon.settingsManager.getBoolean(LmsSettings.firstPersonModelID) &&
				entity == mc.thePlayer &&
				mc.gameSettings.thirdPersonView == 0 &&
				entity.height >= 1.4f &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory)) {
			float v = 4f;
			
			float i = entity.rotationYaw % 360 / 90 * GeneralUtils.pi / 2;
			
			GL11.glTranslatef(GeneralUtils.sin(i) * v / 16, 0, -GeneralUtils.cos(i) * v / 16);
		}
	}
}
