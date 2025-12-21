package net.dravigen.dranimation_lib.mixin;

import net.dravigen.dranimation_lib.packet.PacketUtils;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet250CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public abstract class NetClientHandlerMixin extends NetHandler {
	
	@Shadow
	private Minecraft mc;
	
	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	private void tu_onCustomPayloadC2S(Packet250CustomPayload packet, CallbackInfo ci) {
		switch (packet.channel) {
			case PacketUtils.ID_SYNC_CH -> PacketUtils.handleAnimationSync(packet, this.mc.thePlayer);
			case PacketUtils.DATA_SYNC_CH ->
					PacketUtils.handleAnimationDataToTrackingPlayer(packet, this.mc.renderViewEntity);
			case PacketUtils.EXTRA_CHECK_CH -> PacketUtils.handleExtraIsPresent(packet);
		}
	}
}
