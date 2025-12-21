package net.dravigen.dranimation_lib.mixin;

import net.minecraft.src.Packet250CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Packet250CustomPayload.class)
public class Packet250CustomPayloadMixin {
	@ModifyConstant(method = "readPacketData", constant = @Constant(intValue = 20))
	private int removeDumbLimit(int constant) {
		return 64;
	}
}
