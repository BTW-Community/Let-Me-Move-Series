package net.dravigen.dranimation_lib.utils;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelRenderer;
import net.minecraft.src.ResourceLocation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class AnimationUtils {
	private static final Map<ResourceLocation, BaseAnimation> animationsMap = new HashMap<>();
	public static float delta = 1;
	public static boolean extraIsPresent = false;
	public static boolean serverHasLMM = false;
	
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
		switch (type) {
			case X -> partHolder[0] = rot;
			case Y -> partHolder[1] = rot;
			case Z -> partHolder[2] = rot;
			case Xp -> partHolder[3] = rot;
			case Yp -> partHolder[4] = rot;
			case Zp -> partHolder[5] = rot;
		}
	}
	
	public static void updateAnimationRotation(ModelPartHolder partHolder, ModelBiped model) {
		float deltaTick = AnimationUtils.delta;
		
		lerpModelPart(model.bipedHead, partHolder.head, partHolder.prevHead, deltaTick);
		lerpModelPart(model.bipedBody, partHolder.body, partHolder.prevBody, deltaTick);
		lerpModelPart(model.bipedRightArm, partHolder.rArm, partHolder.prevRArm, deltaTick);
		lerpModelPart(model.bipedLeftArm, partHolder.lArm, partHolder.prevLArm, deltaTick);
		lerpModelPart(model.bipedRightLeg, partHolder.rLeg, partHolder.prevRLeg, deltaTick);
		lerpModelPart(model.bipedLeftLeg, partHolder.lLeg, partHolder.prevLLeg, deltaTick);
		
		syncHeadwear(model);
	}
	
	private static void syncHeadwear(ModelBiped model) {
		model.bipedHeadwear.rotateAngleX = model.bipedHead.rotateAngleX;
		model.bipedHeadwear.rotateAngleY = model.bipedHead.rotateAngleY;
		model.bipedHeadwear.rotateAngleZ = model.bipedHead.rotateAngleZ;
		model.bipedHeadwear.rotationPointX = model.bipedHead.rotationPointX;
		model.bipedHeadwear.rotationPointY = model.bipedHead.rotationPointY;
		model.bipedHeadwear.rotationPointZ = model.bipedHead.rotationPointZ;
	}
	
	private static void lerpModelPart(ModelRenderer renderer, float[] targets, float[] prev, float delta) {
		renderer.rotateAngleX = GeneralUtils.lerpF(delta, prev[0], targets[0]);
		renderer.rotateAngleY = GeneralUtils.lerpF(delta, prev[1], targets[1]);
		renderer.rotateAngleZ = GeneralUtils.lerpF(delta, prev[2], targets[2]);
		
		renderer.rotationPointX = GeneralUtils.lerpF(delta, prev[3], targets[3]);
		renderer.rotationPointY = GeneralUtils.lerpF(delta, prev[4], targets[4]);
		renderer.rotationPointZ = GeneralUtils.lerpF(delta, prev[5], targets[5]);
		
		prev[0] = renderer.rotateAngleX;
		prev[1] = renderer.rotateAngleY;
		prev[2] = renderer.rotateAngleZ;
		prev[3] = renderer.rotationPointX;
		prev[4] = renderer.rotationPointY;
		prev[5] = renderer.rotationPointZ;
	}
	
	public static void offsetAllRotationPoints(float x, float y, float z, float[] head, float[] rArm, float[] lArm,
			float[] rLeg, float[] lLeg, float[] body) {
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
