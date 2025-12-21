package net.dravigen.dranimation_lib.packet;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.io.*;
import java.util.Map;

@SuppressWarnings("unused")
public class PacketUtils {
	public static final String ID_SYNC_CH = "LMM:IdSync";
	public static final String DATA_SYNC_CH = "LMM:DataSync";
	public static final String EXHAUSTION_CH = "LMM:Exhaustion";
	public static final String EXTRA_CHECK_CH = "LMM:extra";
	public static final String LMM_CHECK_CH = "LMM:checkLMM";
	
	public static void animationStoCSync(ResourceLocation ID, NetServerHandler serverHandler) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeUTF(ID.getResourceDomain());
			dos.writeUTF(ID.getResourcePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(ID_SYNC_CH, bos.toByteArray());
		
		serverHandler.sendPacket(packet);
	}
	
	@Environment(EnvType.CLIENT)
	public static void animationCtoSSync(ResourceLocation ID) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeUTF(ID.getResourceDomain());
			dos.writeUTF(ID.getResourcePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(ID_SYNC_CH, bos.toByteArray());
		
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
	}
	
	public static void handleAnimationSync(Packet250CustomPayload packet, EntityPlayer player) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(bis);
			
			ResourceLocation ID = new ResourceLocation(dis.readUTF(), dis.readUTF());
			
			((ICustomMovementEntity) player).lmm_$setAnimation(ID);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Environment(EnvType.CLIENT)
	public static void sendExhaustionToServer(float exhaustion) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeFloat(exhaustion);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(EXHAUSTION_CH, bos.toByteArray());
		
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
	}
	
	public static void handleExhaustionFromClient(Packet250CustomPayload packet, EntityPlayer player) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(bis);
			
			float exhaustion = dis.readFloat();
			
			player.addExhaustion(exhaustion);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Environment(EnvType.CLIENT)
	public static void sendAnimationDataToServer(EntityPlayer player) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
		
		ResourceLocation ID = customPlayer.lmm_$getAnimationID();
		float leaningPitch = customPlayer.lmm_$getLeaningPitch(Minecraft.getMinecraft().getTimer().renderPartialTicks);
		int timeRendered = customPlayer.lmm_$getTimeRendered();
		boolean isOnGround = customPlayer.lmm_$getOnGround();
		boolean isFlying = customPlayer.lmm_$getIsFlying();
		float[] limbs = new float[]{player.limbSwing, player.limbSwingAmount, player.prevLimbSwingAmount};
		
		try {
			dos.writeUTF(ID.getResourceDomain());
			dos.writeUTF(ID.getResourcePath());
			dos.writeFloat(leaningPitch);
			dos.writeInt(timeRendered);
			dos.writeBoolean(isOnGround);
			dos.writeBoolean(isFlying);
			
			for (int i = 0; i < 2; i++) {
				dos.writeFloat(limbs[i]);
			}
			
			for (Map.Entry<ResourceLocation, Integer> animation : customPlayer.lmm_$getAllCooldown().entrySet()) {
				dos.writeUTF(animation.getKey().getResourceDomain());
				dos.writeUTF(animation.getKey().getResourcePath());
				dos.writeInt(animation.getValue());
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(DATA_SYNC_CH, bos.toByteArray());
		
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
	}
	
	public static void handleAnimationDataOnServer(Packet250CustomPayload packet, Entity player) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(bis);
			
			ResourceLocation ID = new ResourceLocation(dis.readUTF(), dis.readUTF());
			float leaningPitch = dis.readFloat();
			int timeRendered = dis.readInt();
			boolean onGround = dis.readBoolean();
			boolean isFlying = dis.readBoolean();
			float[] limbs = new float[]{0, 0};
			
			for (int i = 0; i < 2; i++) {
				limbs[i] = dis.readFloat();
			}
			
			ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
			
			customPlayer.lmm_$setAnimationFromSync(ID);
			customPlayer.lmm_$setLeaningPitch(leaningPitch);
			customPlayer.lmm_$setTimeRendered(timeRendered);
			customPlayer.lmm_$setOnGround(onGround);
			customPlayer.lmm_$setIsFlying(isFlying);
			customPlayer.lmm_$setLimbSwing(limbs);
			
			for (int i = 0; i < Short.MAX_VALUE; i++) {
				try {
					ResourceLocation anim = new ResourceLocation(dis.readUTF(), dis.readUTF());
					int cooldown = dis.readInt();
					
					customPlayer.lmm_$setCooldown(cooldown, anim);
				} catch (EOFException ignored) {
					break;
				}
				
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void sendAnimationDataToTrackingPlayer(EntityPlayerMP player) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
		
		int entityID = player.entityId;
		ResourceLocation ID = customPlayer.lmm_$getAnimationID();
		
		if (ID != null) {
			float leaningPitch = customPlayer.lmm_$getLeaningPitch(1);
			int timeRendered = customPlayer.lmm_$getTimeRendered();
			boolean onGround = customPlayer.lmm_$getOnGround();
			boolean isFlying = customPlayer.lmm_$getIsFlying();
			float[] limbs = customPlayer.lmm_$getLimbSwing();
			
			try {
				dos.writeInt(entityID);
				dos.writeUTF(ID.getResourceDomain());
				dos.writeUTF(ID.getResourcePath());
				dos.writeFloat(leaningPitch);
				dos.writeInt(timeRendered);
				dos.writeBoolean(onGround);
				dos.writeBoolean(isFlying);
				
				for (int i = 0; i < 2; i++) {
					dos.writeFloat(limbs[i]);
				}
				
				for (Map.Entry<ResourceLocation, Integer> animation : customPlayer.lmm_$getAllCooldown().entrySet()) {
					dos.writeUTF(animation.getKey().getResourceDomain());
					dos.writeUTF(animation.getKey().getResourcePath());
					dos.writeInt(animation.getValue());
				}
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			Packet250CustomPayload packet = new Packet250CustomPayload(DATA_SYNC_CH, bos.toByteArray());
			
			player.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(player, packet);
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static void handleAnimationDataToTrackingPlayer(Packet250CustomPayload packet, Entity player) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(bis);
			
			int entityID = dis.readInt();
			ResourceLocation ID = new ResourceLocation(dis.readUTF(), dis.readUTF());
			float leaningPitch = dis.readFloat();
			int timeRendered = dis.readInt();
			boolean onGround = dis.readBoolean();
			boolean isFlying = dis.readBoolean();
			float[] limbs = new float[]{0, 0};
			
			for (int i = 0; i < 2; i++) {
				limbs[i] = dis.readFloat();
			}
			
			Entity entity = getEntityByID(entityID);
			
			if (entity != player) {
				ICustomMovementEntity customPlayer = (ICustomMovementEntity) entity;
				
				customPlayer.lmm_$setAnimationFromSync(ID);
				customPlayer.lmm_$setLeaningPitch(leaningPitch);
				customPlayer.lmm_$setTimeRendered(timeRendered);
				customPlayer.lmm_$setIsFlying(isFlying);
				customPlayer.lmm_$setLimbSwing(limbs);
				customPlayer.lmm_$setOnGround(onGround);
				
				for (int i = 0; i < Short.MAX_VALUE; i++) {
					try {
						ResourceLocation anim = new ResourceLocation(dis.readUTF(), dis.readUTF());
						int cooldown = dis.readInt();
						
						customPlayer.lmm_$setCooldown(cooldown, anim);
					} catch (EOFException ignored) {
						break;
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void sendExtraIsPresent(EntityPlayerMP player, boolean present) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeBoolean(present);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(EXTRA_CHECK_CH, bos.toByteArray());
		
		player.playerNetServerHandler.sendPacketToPlayer(packet);
	}
	
	@Environment(EnvType.CLIENT)
	public static void handleExtraIsPresent(Packet250CustomPayload packet) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(bis);
			
			AnimationUtils.extraIsPresent = dis.readBoolean();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void sendLMMIsPresent(EntityPlayerMP player, boolean present) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeBoolean(present);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(LMM_CHECK_CH, bos.toByteArray());
		
		player.playerNetServerHandler.sendPacketToPlayer(packet);
	}
	
	@Environment(EnvType.CLIENT)
	public static void handleLMMIsPresent(Packet250CustomPayload packet) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			DataInputStream dis = new DataInputStream(bis);
			
			AnimationUtils.serverHasLMM = dis.readBoolean();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private static Entity getEntityByID(int par1) {
		return par1 == Minecraft.getMinecraft().thePlayer.entityId
			   ? Minecraft.getMinecraft().thePlayer
			   : Minecraft.getMinecraft().theWorld.getEntityByID(par1);
	}
}
