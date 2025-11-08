package net.dravigen.let_me_move.mixin.server;

import net.dravigen.dr_api_gen.packet.PacketUtils;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetServerHandler.class)
public abstract class NetServerHandlerMixin extends NetHandler {
	
	@Shadow
	public EntityPlayerMP playerEntity;
	
	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	private void tu_onCustomPayloadC2S(Packet250CustomPayload packet, CallbackInfo ci) {
		switch (packet.channel) {
			case PacketUtils.ANIMATION_SYNC_CHANNEL -> PacketUtils.handleAnimationSync(packet, this.playerEntity);
			case PacketUtils.HUNGER_EXHAUSTION_CHANNEL ->
					PacketUtils.handleExhaustionFromClient(packet, this.playerEntity);
			case PacketUtils.ANIMATION_DATA_SYNC_CHANNEL ->
					PacketUtils.handleAnimationDataOnServer(packet, this.playerEntity);
		}
	}
	
	@Redirect(method = "handleFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerMP;isPlayerSleeping()Z", ordinal = 1))
	private boolean preventIllegalStance(EntityPlayerMP instance) {
		return true;
	}
}
