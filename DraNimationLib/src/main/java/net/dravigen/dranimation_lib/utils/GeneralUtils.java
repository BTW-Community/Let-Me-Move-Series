package net.dravigen.dranimation_lib.utils;

import net.minecraft.src.*;

import java.util.List;

public class GeneralUtils {
	public static final float pi = (float) Math.PI;
	
	public static float pi(int i, int j) {
		return pi * i / j;
	}
	
	public static float sin(float i) {
		return (float) Math.sin(i);
	}
	
	public static float cos(float i) {
		return (float) Math.cos(i);
	}
	
	public static float lerpF(float delta, float start, float end) {
		return start + delta * (end - start);
	}
	
	public static double lerpD(double delta, double start, double end) {
		return start + delta * (end - start);
	}
	
	
	public static boolean isInsideWater(Entity entity) {
		World world = entity.worldObj;
		AxisAlignedBB bb = entity.boundingBox.copy();
		bb.offset(0, 0.2, 0);
		int minY = MathHelper.floor_double(bb.minY + 0.2);
		
		return world.getBlockMaterial(MathHelper.floor_double(entity.posX),
									  minY,
									  MathHelper.floor_double(entity.posZ)) == Material.water;
	}
	
	public static boolean isHeadInsideWater(Entity entity) {
		World world = entity.worldObj;
		AxisAlignedBB bb = entity.boundingBox.copy();
		int eye = MathHelper.floor_double(bb.maxY - 0.3);
		
		return world.getBlockMaterial(MathHelper.floor_double(entity.posX),
									  eye,
									  MathHelper.floor_double(entity.posZ)) == Material.water;
	}
	
	public static boolean isEntityFeetInsideBlock(Entity entity) {
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.boundingBox.minY);
		int z = MathHelper.floor_double(entity.posZ);
		
		return entity.worldObj.isBlockFullCube(x, y, z);
	}
	
	public static boolean isEntityHeadNormalHeightInsideBlock(Entity entity) {
		double x = entity.posX;
		double y = entity.boundingBox.minY + 1;
		double z = entity.posZ;
		double halfWidth = entity.width / 2;
		
		return !entity.worldObj.getCollidingBoundingBoxes(entity,
														  new AxisAlignedBB(x - halfWidth,
																			y,
																			z - halfWidth,
																			x + halfWidth,
																			y + 0.5,
																			z + halfWidth)).isEmpty();
	}
	
	public static boolean isEntityHeadInsideBlock(Entity entity, double yOff) {
		double x = entity.posX;
		double y = entity.boundingBox.minY + 1;
		double z = entity.posZ;
		double halfWidth = entity.width / 2;
		
		return !entity.worldObj.getCollidingBoundingBoxes(entity,
														  new AxisAlignedBB(x - halfWidth,
																			y,
																			z - halfWidth,
																			x + halfWidth,
																			y + yOff,
																			z + halfWidth)).isEmpty();
	}
	
	public static float lerpAngle(float angleOne, float angleTwo, float magnitude) {
		float f = (magnitude - angleTwo) % (float) (Math.PI * 2);
		
		if (f < (float) -Math.PI) {
			f += (float) (Math.PI * 2);
		}
		
		if (f >= (float) Math.PI) {
			f -= (float) (Math.PI * 2);
		}
		
		return angleTwo + angleOne * f;
	}
	
	public static float method_2807(float f) {
		return -65.0F * f + f * f;
	}
	
	public static float incrementUntilGoal(float currentValue, float goalValue, float easeFactor) {
		
		float difference = goalValue - currentValue;
		
		float stepSize = difference * easeFactor;
		
		return currentValue + stepSize;
	}
	
	public static float incrementAngleUntilGoal(float currentValue, float goalValue, float easeFactor) {
		
		float difference = goalValue - currentValue;
		
		difference = difference % 360.0F;
		
		if (difference > 180.0F) {
			difference -= 360.0F;
		}
		else if (difference < -180.0F) {
			difference += 360.0F;
		}
		
		float stepSize = difference * easeFactor;
		
		float newValue = currentValue + stepSize;
		
		newValue = newValue % 360.0F;
		
		if (newValue < 0) {
			newValue += 360.0F;
		}
		
		return newValue;
	}
	
	public static coords getWallSide(Entity entity, double yOff, double height) {
		double x = entity.posX;
		double y = entity.boundingBox.minY + yOff;
		double z = entity.posZ;
		
		World world = entity.worldObj;
		
		boolean wallEast = !world.getCollidingBoundingBoxes(entity,
															new AxisAlignedBB(x + entity.width / 2 + 0.1,
																			  y,
																			  z - 0.25,
																			  x + entity.width / 2 + 0.25,
																			  y + height,
																			  z + 0.25)).isEmpty();
		boolean wallWest = !world.getCollidingBoundingBoxes(entity,
															new AxisAlignedBB(x - entity.width / 2 - 0.1,
																			  y,
																			  z - 0.25,
																			  x - entity.width / 2 - 0.25,
																			  y + height,
																			  z + 0.25)).isEmpty();
		boolean wallSouth = !world.getCollidingBoundingBoxes(entity,
															 new AxisAlignedBB(x - 0.25,
																			   y,
																			   z + entity.width / 2 + 0.1,
																			   x + 0.25,
																			   y + height,
																			   z + entity.width / 2 + 0.25)).isEmpty();
		boolean wallNorth = !world.getCollidingBoundingBoxes(entity,
															 new AxisAlignedBB(x - 0.25,
																			   y,
																			   z - entity.width / 2 - 0.1,
																			   x + 0.25,
																			   y + height,
																			   z + entity.width / 2 - 0.25)).isEmpty();
		
		int facing = getFacing(entity);
		
		switch (facing) {
			case 0:
				if (wallNorth) return coords.NORTH;
			case 1:
				if (wallEast) return coords.EAST;
			case 2:
				if (wallSouth) return coords.SOUTH;
			case 3:
				if (wallWest) return coords.WEST;
		}
		
		return wallEast
			   ? coords.EAST
			   : wallWest ? coords.WEST : wallSouth ? coords.SOUTH : wallNorth ? coords.NORTH : null;
	}
	
	public static double getWallTopYIfEmptySpace(Entity entity) {
		coords side = getWallSide(entity, 0, entity.height);
		
		if (side == null) return -1;
		
		double x = entity.posX + (side == coords.EAST ? 1 : side == coords.WEST ? -1 : 0);
		double z = entity.posZ + (side == coords.SOUTH ? 1 : side == coords.NORTH ? -1 : 0);
		
		List<AxisAlignedBB> wall = getBlockBoundCenteredList(entity, x, entity.boundingBox.minY, z, entity.height);
		
		if (!wall.isEmpty()) {
			AxisAlignedBB top = wall.get(wall.size() - 1);
			
			if (getBlockBoundCentered(entity.worldObj, entity, x, top.maxY, z, 1.4) == null) {
				
				return top.maxY;
			}
		}
		
		return -1;
	}
	
	public static boolean checkIfEntityFacingWall(Entity entity) {
		coords wall = getWallSide(entity, 0, entity.height);
		int facing = getFacing(entity);
		
		if (wall == null) return false;
		
		return wall == coords.NORTH && facing == 0 ||
				wall == coords.EAST && facing == 1 ||
				wall == coords.SOUTH && facing == 2 ||
				wall == coords.WEST && facing == 3;
	}
	
	private static int getFacing(Entity entity) {
		float yaw = (entity.getRotationYawHead() + 180) % 360;
		
		yaw = yaw < 0 ? 360 + yaw : yaw;
		
		return Math.round(yaw / 90) % 4;
	}
	
	@SuppressWarnings("rawtypes")
	private static AxisAlignedBB getBlockBoundCentered(World world, Entity entity, double x, double y, double z,
			double height) {
		List boundingBoxes = world.getCollidingBoundingBoxes(entity,
															 new AxisAlignedBB(x - 0.25,
																			   y,
																			   z - 0.25,
																			   x + 0.25,
																			   y + height,
																			   z + 0.25));
		
		if (boundingBoxes.isEmpty()) return null;
		
		return (AxisAlignedBB) boundingBoxes.get(boundingBoxes.size() - 1);
	}
	
	@SuppressWarnings("unchecked")
	private static List<AxisAlignedBB> getBlockBoundCenteredList(Entity entity, double x, double y, double z,
			double height) {
		return (List<AxisAlignedBB>) entity.worldObj.getCollidingBoundingBoxes(entity,
																			   new AxisAlignedBB(x - 0.25,
																								 y,
																								 z - 0.25,
																								 x + 0.25,
																								 y + height,
																								 z + 0.25));
	}
	
	// Thresholds used to classify movement based on the dot product value
	private static final float FORWARD_THRESHOLD = 0.1f;
	private static final float BACKWARD_THRESHOLD = -0.1f;
	
	public enum MovementType {
		IDLE, FORWARD, BACKWARD, STRAFING, MIXED
	}
	
	/**
	 * Determines the primary direction of horizontal movement relative to the player's view.
	 * * @param player The entity whose movement is being analyzed.
	 * @return The classified MovementType.
	 */
	public static MovementType getRelativeMovement(EntityLivingBase player) {
		
		// --- 1. Calculate Movement Vector (M) in XZ Plane ---
		
		// Horizontal change in position (Current Pos - Previous Pos)
		double dx = player.posX - player.prevPosX;
		double dz = player.posZ - player.prevPosZ;
		
		double magnitudeSq = (dx * dx) + (dz * dz);
		
		// If movement is negligible, the player is IDLE
		if (magnitudeSq < 0.0001) {
			return MovementType.IDLE;
		}
		
		// Calculate the length (magnitude) of the movement vector
		double magnitude = Math.sqrt(magnitudeSq);
		
		// Normalized Movement Vector (M_norm)
		float mx = (float)(dx / magnitude);
		float mz = (float)(dz / magnitude);
		
		// --- 2. Calculate View Vector (V) ---
		
		// Yaw angle (rotation around Y-axis) in radians
		float yawRad = player.rotationYaw * (float) (Math.PI / 180.0F);
		
		// The standard Minecraft XZ unit vector for player look direction
		// NOTE: Signs may vary based on your specific coordinate system!
		float vx = -MathHelper.sin(yawRad);
		float vz = MathHelper.cos(yawRad);
		
		// --- 3. Calculate Dot Product (Alignment) ---
		
		// Dot Product = (Mx * Vx) + (Mz * Vz)
		float dotProduct = (mx * vx) + (mz * vz);
		
		// --- 4. Classify Movement ---
		
		if (dotProduct > FORWARD_THRESHOLD) {
			// Dot product close to 1: Movement is aligned with view direction (0 degrees)
			return MovementType.FORWARD;
		}
		
		if (dotProduct < BACKWARD_THRESHOLD) {
			// Dot product close to -1: Movement is opposite to view direction (180 degrees)
			return MovementType.BACKWARD;
		}
		
		// To check for strafing, we need the "side-to-side" dot product.
		// The vector perpendicular to V is (-Vz, Vx) or (Vz, -Vx).
		// Let's check alignment with the rightward strafe vector (V_right)
		float vx_right = vz;
		float vz_right = -vx;
		
		float strafeDotProduct = (mx * vx_right) + (mz * vz_right);
		
		if (strafeDotProduct > FORWARD_THRESHOLD || strafeDotProduct < BACKWARD_THRESHOLD) {
			// If the movement vector aligns with the right or left strafe vector (dot product near 1 or -1)
			return MovementType.STRAFING;
		}
		
		// If it's movement, but not a clear Forward, Backward, or Strafe, it's mixed (diagonal)
		if (magnitudeSq > 0.005) {
			return MovementType.MIXED;
		}
		
		return MovementType.IDLE;
	}
	
	/**
	 * Calculates the forward/backward and strafe components of a player's movement
	 * relative to their current view direction.
	 *
	 * @param player The entity whose movement is being analyzed.
	 * @return A float array: [0] = Forward/Backward Value, [1] = Strafe (Left/Right) Value.
	 */
	public static float[] getMovementComponents(EntityLivingBase player) {
		
		// --- 1. Calculate Movement Vector (M) ---
		
		double dx = player.posX - player.prevPosX;
		double dz = player.posZ - player.prevPosZ;
		
		double magnitudeSq = (dx * dx) + (dz * dz);
		
		// If movement is negligible, return [0.0, 0.0] for idle state
		if (magnitudeSq < 0.001) {
			return new float[] {0.0f, 0.0f};
		}
		
		// Calculate the magnitude and normalize the movement vector (M_norm)
		float magnitude = (float) Math.sqrt(magnitudeSq);
		float M_norm_x = (float)(dx / magnitude);
		float M_norm_z = (float)(dz / magnitude);
		
		// --- 2. Calculate View Vectors (V_fwd and V_right) ---
		
		// Convert yaw to radians
		float yawRad = player.rotationYaw * (float) (Math.PI / 180.0F);
		
		// Forward Vector (V_fwd) - Normalized Look Direction
		// The signs here are standard for Minecraft's XZ plane projection:
		float VfwdX = -MathHelper.sin(yawRad);
		float VfwdZ = MathHelper.cos(yawRad);
		
		// Right Vector (V_right) - Perpendicular to the Forward Vector
		float VrightX = MathHelper.cos(yawRad);
		float VrightZ = MathHelper.sin(yawRad);
		
		// --- 3. Calculate Component Values using Dot Products ---
		
		// A. Forward/Backward Component (Dot Product with V_fwd)
		// Range: 1.0 (Forward) to -1.0 (Backward)
		float forwardValue = (M_norm_x * VfwdX) + (M_norm_z * VfwdZ);
		
		// B. Strafe Component (Dot Product with V_right)
		// Range: 1.0 (Right Strafe) to -1.0 (Left Strafe)
		float strafeValue = (M_norm_x * VrightX) + (M_norm_z * VrightZ);
		
		// The values are now scaled between -1.0 and 1.0 based on how
		// much of the movement is aligned with each axis.
		return new float[] {forwardValue, strafeValue};
	}
	
	public enum coords {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
}
