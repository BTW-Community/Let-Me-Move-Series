package net.dravigen.let_me_see.mixin;

import api.client.CustomUpdatingTexture;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.let_me_see.LetMeSeeAddon;
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

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin extends RendererLivingEntity {
	
	@Shadow
	private ModelBiped modelBipedMain;
	
	public RenderPlayerMixin(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/TileEntitySkullRenderer;func_82393_a(FFFIFILjava/lang/String;)V"))
	private void disableHeadIf1stPers(TileEntitySkullRenderer instance, float v, float par1, float par2, int par3,
			float par4, int par5, String par6, AbstractClientPlayer player) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (player == mc.thePlayer && LetMeSeeAddon.isIsCustomFirstPerson(mc)) return;
	
		instance.func_82393_a(v, par1, par2, par3, par4, par5, par6);
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
		
		if (LetMeSeeAddon.isIsCustomFirstPerson(mc) && heldItem != null && (heldItem.itemID == Item.map.itemID)) {
			this.modelBipedMain.bipedRightArm.showModel = true;
			this.modelBipedMain.bipedLeftArm.showModel = true;
		}
	}
	
	@Inject(method = "rotatePlayer", at = @At("HEAD"))
	private void offsetPlayerIn1stPerson(AbstractClientPlayer entity, float par2, float par3, float par4,
			CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		if (LetMeSeeAddon.isIsCustomFirstPerson(mc) &&
				entity == mc.thePlayer &&
				entity.height >= 1.4f) {
			float v = (float) (4f * LMS_Settings.HEAD_OFFSET.getDouble());
			
			float i = entity.rotationYaw % 360 / 90 * GeneralUtils.pi / 2;
			
			GL11.glTranslatef(GeneralUtils.sin(i) * v / 16, 0, -GeneralUtils.cos(i) * v / 16);
		}
	}
	
	@ModifyArg(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 11), index = 0)
	private float verticalBow(float angle) {
		return 0;
	}
	
	@ModifyArg(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 6), index = 0)
	private float offsetBowInnerHand(float angle) {
		return 2 / 16f;
	}
}
