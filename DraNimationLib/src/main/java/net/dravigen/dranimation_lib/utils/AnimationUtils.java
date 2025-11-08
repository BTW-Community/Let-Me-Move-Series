package net.dravigen.dranimation_lib.utils;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.minecraft.src.*;

import java.util.*;
import java.util.stream.Collectors;

public class AnimationUtils {
	private static final Map<ResourceLocation, BaseAnimation> animationsMap = new HashMap<>();
	
	public static Map<ResourceLocation, BaseAnimation> getAnimationsMap() {
		return animationsMap.entrySet()
				.stream()
				.sorted(Comparator.comparingInt(animation -> animation.getValue().priority))
				.collect(Collectors.toMap(Map.Entry::getKey,
										  Map.Entry::getValue,
										  (oldValue, newValue) -> oldValue,
										  LinkedHashMap::new));
	}
	
	public static BaseAnimation getAnimationFromID(ResourceLocation ID) {
		return animationsMap.get(ID);
	}
	
	public static void registerAnimation(BaseAnimation animation, int priority) {
		animation.priority = priority;
		animationsMap.put(animation.getID(), animation);
	}
	
	public static void smoothRotateAll(float[] partHolder, float rotX, float rotY, float rotZ, float factor) {
		smoothRotate(partHolder, AnimationUtils.type.X, rotX, factor);
		smoothRotate(partHolder, AnimationUtils.type.Y, rotY, factor);
		smoothRotate(partHolder, AnimationUtils.type.Z, rotZ, factor);
	}
	
	public static void smoothRotateAll(float[] partHolder, float[] rots, float factorRot, float factorMove) {
		smoothRotate(partHolder, type.X, rots[0], factorRot);
		smoothRotate(partHolder, type.Y, rots[1], factorRot);
		smoothRotate(partHolder, type.Z, rots[2], factorRot);
		smoothRotate(partHolder, type.Xp, rots[3], factorMove);
		smoothRotate(partHolder, type.Yp, rots[4], factorMove);
		smoothRotate(partHolder, type.Zp, rots[5], factorMove);
	}
	
	
	public static void smoothRotateAll(float[] partHolder, float[] rots, float factor) {
		smoothRotate(partHolder, type.X, rots[0], factor);
		smoothRotate(partHolder, type.Y, rots[1], factor);
		smoothRotate(partHolder, type.Z, rots[2], factor);
		smoothRotate(partHolder, type.Xp, rots[3], factor);
		smoothRotate(partHolder, type.Yp, rots[4], factor);
		smoothRotate(partHolder, type.Zp, rots[5], factor);
	}
	
	public static void smoothRotate(float[] partHolder, AnimationUtils.type type, float rot, float factor) {
		if (type == AnimationUtils.type.X) {
			partHolder[0] = GeneralUtils.incrementUntilGoal(partHolder[0], rot, factor);
		}
		else if (type == AnimationUtils.type.Y) {
			partHolder[1] = GeneralUtils.incrementUntilGoal(partHolder[1], rot, factor);
		}
		else if (type == AnimationUtils.type.Z) {
			partHolder[2] = GeneralUtils.incrementUntilGoal(partHolder[2], rot, factor);
		}
		else if (type == AnimationUtils.type.Xp) {
			partHolder[3] = GeneralUtils.incrementUntilGoal(partHolder[3], rot, factor);
		}
		else if (type == AnimationUtils.type.Yp) {
			partHolder[4] = GeneralUtils.incrementUntilGoal(partHolder[4], rot, factor);
		}
		else if (type == AnimationUtils.type.Zp) {
			partHolder[5] = GeneralUtils.incrementUntilGoal(partHolder[5], rot, factor);
		}
	}
	
	public static void updateAnimationRotation(ModelPartHolder partHolder, ModelBiped model) {
		model.bipedHead.rotateAngleX = partHolder.head[0];
		model.bipedHead.rotateAngleY = partHolder.head[1];
		model.bipedHead.rotateAngleZ = partHolder.head[2];
		model.bipedHead.rotationPointX = partHolder.head[3];
		model.bipedHead.rotationPointY = partHolder.head[4];
		model.bipedHead.rotationPointZ = partHolder.head[5];
		
		model.bipedBody.rotateAngleX = partHolder.body[0];
		model.bipedBody.rotateAngleY = partHolder.body[1];
		model.bipedBody.rotateAngleZ = partHolder.body[2];
		model.bipedBody.rotationPointX = partHolder.body[3];
		model.bipedBody.rotationPointY = partHolder.body[4];
		model.bipedBody.rotationPointZ = partHolder.body[5];
		
		model.bipedCloak.rotateAngleY = partHolder.body[1];
		
		model.bipedRightArm.rotateAngleX = partHolder.rArm[0];
		model.bipedRightArm.rotateAngleY = partHolder.rArm[1];
		model.bipedRightArm.rotateAngleZ = partHolder.rArm[2];
		model.bipedRightArm.rotationPointX = partHolder.rArm[3];
		model.bipedRightArm.rotationPointY = partHolder.rArm[4];
		model.bipedRightArm.rotationPointZ = partHolder.rArm[5];
		
		model.bipedLeftArm.rotateAngleX = partHolder.lArm[0];
		model.bipedLeftArm.rotateAngleY = partHolder.lArm[1];
		model.bipedLeftArm.rotateAngleZ = partHolder.lArm[2];
		model.bipedLeftArm.rotationPointX = partHolder.lArm[3];
		model.bipedLeftArm.rotationPointY = partHolder.lArm[4];
		model.bipedLeftArm.rotationPointZ = partHolder.lArm[5];
		
		model.bipedRightLeg.rotateAngleX = partHolder.rLeg[0];
		model.bipedRightLeg.rotateAngleY = partHolder.rLeg[1];
		model.bipedRightLeg.rotateAngleZ = partHolder.rLeg[2];
		model.bipedRightLeg.rotationPointX = partHolder.rLeg[3];
		model.bipedRightLeg.rotationPointY = partHolder.rLeg[4];
		model.bipedRightLeg.rotationPointZ = partHolder.rLeg[5];
		
		model.bipedLeftLeg.rotateAngleX = partHolder.lLeg[0];
		model.bipedLeftLeg.rotateAngleY = partHolder.lLeg[1];
		model.bipedLeftLeg.rotateAngleZ = partHolder.lLeg[2];
		model.bipedLeftLeg.rotationPointX = partHolder.lLeg[3];
		model.bipedLeftLeg.rotationPointY = partHolder.lLeg[4];
		model.bipedLeftLeg.rotationPointZ = partHolder.lLeg[5];
		
		model.bipedHeadwear.rotateAngleX = model.bipedHead.rotateAngleX;
		model.bipedHeadwear.rotateAngleY = model.bipedHead.rotateAngleY;
		model.bipedHeadwear.rotateAngleZ = model.bipedHead.rotateAngleZ;
		model.bipedHeadwear.rotationPointX = model.bipedHead.rotationPointX;
		model.bipedHeadwear.rotationPointY = model.bipedHead.rotationPointY;
		model.bipedHeadwear.rotationPointZ = model.bipedHead.rotationPointZ;
	}
	
	public static void offsetAllRotationPoints(float x, float y, float z, float[] head, float[] rArm, float[] lArm, float[] rLeg,
			float[] lLeg, float[] body) {
		head[3] += x;
		head[4] += y;
		head[5] += z;
		
		body[3] += x;
		body[4] += y;
		body[5] += z;
		
		rArm[3] += x;
		rArm[4] += y;
		rArm[5] += z;
		
		lArm[3] += x;
		lArm[4] += y;
		lArm[5] += z;
		
		rLeg[3] += x;
		rLeg[4] += y;
		rLeg[5] += z;
		
		lLeg[3] += x;
		lLeg[4] += y;
		lLeg[5] += z;
	}
	
	public enum type {
		X,
		Y,
		Z,
		Xp,
		Yp,
		Zp
	}
}
