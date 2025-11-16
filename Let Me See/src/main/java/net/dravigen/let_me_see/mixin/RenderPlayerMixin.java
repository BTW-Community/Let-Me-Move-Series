package net.dravigen.let_me_see.mixin;

import btw.client.texture.CustomUpdatingTexture;
import net.dravigen.let_me_see.LetMeSeeAddon;
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
public abstract class RenderPlayerMixin extends RendererLivingEntity{
	/*
	@Inject(method = "renderEquippedItems", at = @At("HEAD"), cancellable = true)
	private void a(EntityLivingBase par1EntityLivingBase, float par2, CallbackInfo ci) {
		//ci.cancel();
	}*/
	/*
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemRenderer;renderItem(Lnet/minecraft/src/EntityLivingBase;Lnet/minecraft/src/ItemStack;I)V", ordinal = 1))
	private void b(ItemRenderer instance, EntityLivingBase var15, ItemStack var16, int var5) {
	
	}*/
	
	@Shadow
	private ModelBiped modelBipedMain;
	
	public RenderPlayerMixin(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemRenderer;renderItem(Lnet/minecraft/src/EntityLivingBase;Lnet/minecraft/src/ItemStack;I)V", ordinal = 2))
	private void c(ItemRenderer instance, EntityLivingBase var15, ItemStack var16, int var5) {
		ItemStack var8 = var15.getHeldItem();
		Icon icon = var8.getItem().getIconFromDamage(0);
		if (icon instanceof CustomUpdatingTexture) {
			CustomUpdatingTexture customUpdateTexture = (CustomUpdatingTexture)((Object)icon);
			customUpdateTexture.updateActive(CustomUpdatingTexture.FIRST_PERSON);
		}
		instance.renderItem(var15, var16, var5);
		
		icon = var8.getItem().getIconFromDamage(0);
		if (icon instanceof CustomUpdatingTexture) {
			CustomUpdatingTexture customUpdateTexture = (CustomUpdatingTexture)((Object)icon);
			customUpdateTexture.updateInert(CustomUpdatingTexture.FIRST_PERSON);
		}
	}
	
	@Inject(method = "renderFirstPersonArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;render(F)V"))
	private void d(EntityPlayer player, CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack heldItem = mc.thePlayer.getHeldItem();
		
		if (LetMeSeeAddon.enable3DFirstPerson &&
				mc.gameSettings.thirdPersonView == 0 &&
				heldItem != null &&
				(heldItem.itemID == Item.map.itemID) &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory)) {
			this.modelBipedMain.bipedRightArm.showModel = true;
			this.modelBipedMain.bipedLeftArm.showModel = true;
		}
	}
	
	/*
	@ModifyArg(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal =  11), index = 0)
	private float a(float angle) {
		
		return 20.0f;
	}
	
	@ModifyArg(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal =  6), index = 0)
	private float b(float angle) {
		return 0.3f;
	}*/
}
