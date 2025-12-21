package net.dravigen.dranimation_lib.utils;

@SuppressWarnings("unused")
public class ModelPartHolder {
	float[] head = new float[]{0, 0, 0, 0, 0, 0};
	float[] body = new float[]{0, 0, 0, 0, 12, 0};
	float[] rArm = new float[]{0, 0, 0, -5, 2, 0};
	float[] lArm = new float[]{0, 0, 0, 5, 2, 0};
	float[] rLeg = new float[]{0, 0, 0, -1.9f, 12, 0.1f};
	float[] lLeg = new float[]{0, 0, 0, 1.9f, 12, 0.1f};
	
	float[] prevHead = new float[]{0, 0, 0, 0, 0, 0};
	float[] prevBody = new float[]{0, 0, 0, 0, 12, 0};
	float[] prevRArm = new float[]{0, 0, 0, -5, 2, 0};
	float[] prevLArm = new float[]{0, 0, 0, 5, 2, 0};
	float[] prevRLeg = new float[]{0, 0, 0, -1.9f, 12, 0.1f};
	float[] prevLLeg = new float[]{0, 0, 0, 1.9f, 12, 0.1f};
	
	public void resetAnimationRotationPoints() {
		head = new float[]{head[0], head[1], head[2], 0, 0, 0};
		body = new float[]{head[0], head[1], head[2], 0, 12, 0};
		rArm = new float[]{head[0], head[1], head[2], -5, 2, 0};
		lArm = new float[]{head[0], head[1], head[2], 5, 2, 0};
		rLeg = new float[]{head[0], head[1], head[2], -1.9f, 12, 0.1f};
		lLeg = new float[]{head[0], head[1], head[2], 1.9f, 12, 0.1f};
	}
	
	public float[] getHead() {
		return head;
	}
	
	public float[] getBody() {
		return body;
	}
	
	public float[] getrArm() {
		return rArm;
	}
	
	public float[] getlArm() {
		return lArm;
	}
	
	public float[] getrLeg() {
		return rLeg;
	}
	
	public float[] getlLeg() {
		return lLeg;
	}
	
}
