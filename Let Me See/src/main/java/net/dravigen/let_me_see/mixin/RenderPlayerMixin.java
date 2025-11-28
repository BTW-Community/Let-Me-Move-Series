package net.dravigen.let_me_see.mixin;

import btw.client.texture.CustomUpdatingTexture;
import btw.item.items.ArmorItemMod;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.let_me_see.config.LMS_Settings;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin extends RendererLivingEntity {
	
	@Shadow
	private ModelBiped modelBipedMain;
	
	@Shadow
	private ModelBiped modelArmor;
	
	@Shadow
	private ModelBiped modelArmorChestplate;
	
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
		
		if (LMS_Settings.FIRST_PERSON_MODEL.getBool() &&
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
		if (LMS_Settings.FIRST_PERSON_MODEL.getBool() &&
				entity == mc.thePlayer &&
				mc.gameSettings.thirdPersonView == 0 &&
				entity.height >= 1.4f &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory)) {
			float v = (float) (4f * LMS_Settings.HEAD_OFFSET.getDouble());
			
			float i = entity.rotationYaw % 360 / 90 * GeneralUtils.pi / 2;
			
			GL11.glTranslatef(GeneralUtils.sin(i) * v / 16, 0, -GeneralUtils.cos(i) * v / 16);
		}
	}
	
	@ModifyArg(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 11), index = 0)
	private float a(float angle) {
		return 0;
	}
	
	@ModifyArg(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 6), index = 0)
	private float b(float angle) {
		return 2 / 16f;
	}
	
	@Inject(method = "shouldRenderPassModArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderPlayer;setRenderPassModel(Lnet/minecraft/src/ModelBase;)V"))
	private void shouldRenderParts(ItemStack stack, int iArmorSlot, ArmorItemMod armorItem,
			CallbackInfoReturnable<Integer> cir) {
		ModelBiped model = iArmorSlot == 2 ? this.modelArmor : this.modelArmorChestplate;
		
		Minecraft mc = Minecraft.getMinecraft();
		Entity entity = this.renderManager.livingPlayer;
		
		if (LMS_Settings.FIRST_PERSON_MODEL.getBool() &&
				entity == mc.thePlayer &&
				mc.gameSettings.thirdPersonView == 0 &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory)) {
			model.bipedBody.showModel = entity.height > 1.4 && (iArmorSlot == 1 || iArmorSlot == 2);
			model.bipedHead.showModel = false;
			
			ItemStack heldItem = mc.thePlayer.getHeldItem();
			if (heldItem != null && (heldItem.itemID == Item.map.itemID)) {
				model.bipedRightArm.showModel = false;
				model.bipedLeftArm.showModel = false;
			}
			else {
				model.bipedRightArm.showModel = iArmorSlot == 1;
				model.bipedLeftArm.showModel = iArmorSlot == 1;
			}
		}
		else {
			model.bipedHead.showModel = iArmorSlot == 0;
			model.bipedBody.showModel = iArmorSlot == 1 || iArmorSlot == 2;
			model.bipedRightArm.showModel = iArmorSlot == 1;
			model.bipedLeftArm.showModel = iArmorSlot == 1;
		}
		
		model.bipedHeadwear.showModel = model.bipedHead.showModel;
		model.bipedCloak.showModel = model.bipedBody.showModel;
	}
}
