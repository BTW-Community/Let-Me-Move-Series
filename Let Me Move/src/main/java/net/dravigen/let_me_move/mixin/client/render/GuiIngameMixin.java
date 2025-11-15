package net.dravigen.let_me_move.mixin.client.render;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(GuiIngame.class)
public abstract class GuiIngameMixin extends Gui {
	
	@Shadow
	@Final
	private Minecraft mc;
	
	
	@Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiIngame;renderDebugOverlay()V"))
	private void renderAnimationTexts(float par1, boolean par2, int par3, int par4, CallbackInfo ci) {
		EntityClientPlayerMP player = mc.thePlayer;
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
		BaseAnimation animation = customPlayer.lmm_$getAnimation();
		FontRenderer fontRenderer = mc.fontRenderer;
		ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings,
																 this.mc.displayWidth,
																 this.mc.displayHeight);
		int scaledWidth = scaledresolution.getScaledWidth();
		int scaledHeight = scaledresolution.getScaledHeight();
		
		String name = animation.getName(player);
		
		int y = scaledHeight - fontRenderer.FONT_HEIGHT - 1;
		
		this.drawString(fontRenderer,
						name,
						scaledWidth - fontRenderer.getStringWidth(name) - 2,
						y,
						Color.white.getRGB());
		
		y -= 5;
		
		Map<ResourceLocation, Integer> cooldownAnims = customPlayer.lmm_$getAllCooldown();
		
		cooldownAnims = cooldownAnims.entrySet()
				.stream()
				.sorted(Comparator.comparingInt(Map.Entry::getValue))
				.collect(Collectors.toMap(Map.Entry::getKey,
										  Map.Entry::getValue,
										  (oldValue, newValue) -> oldValue,
										  LinkedHashMap::new));
		
		
		for (ResourceLocation animCoolId : cooldownAnims.keySet()) {
			int cooldown = cooldownAnims.get(animCoolId);
			
			if (cooldown > 0) {
				BaseAnimation animCool = AnimationUtils.getAnimationFromID(animCoolId);
				String string = animCool.getName(player);
				
				int cooldownWidth = 60;
				float delta = (float) cooldownWidth / animCool.maxCooldown;
				y -= fontRenderer.FONT_HEIGHT - 1;
				
				GL11.glPushMatrix();
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				
				
				int var1 = (int) (cooldown * delta);
				float diff = (float) cooldown / animCool.maxCooldown;
				Color color = diff <= 2 / 3f ? diff <= 1 / 3f ? Color.green : Color.yellow : Color.red;
				
				Gui.drawRect(scaledWidth - 2 - cooldownWidth,
							 y - 1,
							 scaledWidth - 2,
							 y + fontRenderer.FONT_HEIGHT,
							 50 << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue());
				
				for (int i = 0; i <= var1; i++) {
					float diff2 = (float) i / cooldownWidth;
					Color color2 = diff2 < 2 / 3f ? diff2 < 1 / 3f ? Color.green : Color.yellow : Color.red;
					Gui.drawRect(scaledWidth - 2 - i,
								 y - 1,
								 scaledWidth - 2,
								 y + fontRenderer.FONT_HEIGHT,
								 80 << 24 | color2.getRed() << 16 | color2.getGreen() << 8 | color2.getBlue());
				}
				
				GL11.glDisable(3042);
				GL11.glPopMatrix();
				
				this.drawCenteredString(fontRenderer, string, scaledWidth - 2 - cooldownWidth / 2, y, color.getRGB());
				
				y -= 3;
			}
		}
	}
}
