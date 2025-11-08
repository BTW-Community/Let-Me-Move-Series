package net.dravigen.dranimation_lib.packet;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

import java.io.*;
import java.util.Map;

public class PacketUtils {
	public static final String ANIMATION_SYNC_CHANNEL = "LMM:AnimIDSync";
	public static final String ANIMATION_DATA_SYNC_CHANNEL = "LMM:AnimDataSync";
	public static final String HUNGER_EXHAUSTION_CHANNEL = "LMM:Exhaustion";
	
	public static void animationStoCSync(ResourceLocation ID, NetServerHandler serverHandler) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeUTF(ID.getResourceDomain());
			dos.writeUTF(ID.getResourcePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(ANIMATION_SYNC_CHANNEL, bos.toByteArray());
		
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
		
		Packet250CustomPayload packet = new Packet250CustomPayload(ANIMATION_SYNC_CHANNEL, bos.toByteArray());
		
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
		
		Packet250CustomPayload packet = new Packet250CustomPayload(HUNGER_EXHAUSTION_CHANNEL, bos.toByteArray());
		
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
		
		try {
			dos.writeUTF(ID.getResourceDomain());
			dos.writeUTF(ID.getResourcePath());
			dos.writeFloat(leaningPitch);
			dos.writeInt(timeRendered);
			dos.writeBoolean(isOnGround);
			dos.writeBoolean(isFlying);
			
			for (Map.Entry<ResourceLocation, Integer> animation : customPlayer.lmm_$getAllCooldown().entrySet()) {
				dos.writeUTF(animation.getKey().getResourceDomain());
				dos.writeUTF(animation.getKey().getResourcePath());
				dos.writeInt(animation.getValue());
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(ANIMATION_DATA_SYNC_CHANNEL, bos.toByteArray());
		
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
			
			ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
			
			customPlayer.lmm_$setAnimationFromSync(ID);
			customPlayer.lmm_$setLeaningPitch(leaningPitch);
			customPlayer.lmm_$setTimeRendered(timeRendered);
			customPlayer.lmm_$setOnGround(onGround);
			customPlayer.lmm_$setIsFlying(isFlying);
			
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
			
			try {
				dos.writeInt(entityID);
				dos.writeUTF(ID.getResourceDomain());
				dos.writeUTF(ID.getResourcePath());
				dos.writeFloat(leaningPitch);
				dos.writeInt(timeRendered);
				dos.writeBoolean(onGround);
				dos.writeBoolean(isFlying);
				
				for (Map.Entry<ResourceLocation, Integer> animation : customPlayer.lmm_$getAllCooldown().entrySet()) {
					dos.writeUTF(animation.getKey().getResourceDomain());
					dos.writeUTF(animation.getKey().getResourcePath());
					dos.writeInt(animation.getValue());
				}
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			Packet250CustomPayload packet = new Packet250CustomPayload(ANIMATION_DATA_SYNC_CHANNEL, bos.toByteArray());
			
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
			
			Entity entity = getEntityByID(entityID);
			
			if (entity != player) {
				ICustomMovementEntity customPlayer = (ICustomMovementEntity) entity;
				
				customPlayer.lmm_$setAnimationFromSync(ID);
				customPlayer.lmm_$setLeaningPitch(leaningPitch);
				customPlayer.lmm_$setTimeRendered(timeRendered);
				customPlayer.lmm_$setOnGround(onGround);
				customPlayer.lmm_$setIsFlying(isFlying);
				
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
	
	
	@Environment(EnvType.CLIENT)
	private static Entity getEntityByID(int par1) {
		return par1 == Minecraft.getMinecraft().thePlayer.entityId ? Minecraft.getMinecraft().thePlayer : Minecraft.getMinecraft().theWorld.getEntityByID(par1);
	}
}
