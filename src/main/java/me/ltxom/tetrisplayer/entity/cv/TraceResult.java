package me.ltxom.tetrisplayer.entity.cv;

public class TraceResult {
	private float confidence;
	private int value;

	public TraceResult(float confidence, int value) {
		this.confidence = confidence;
		this.value = value;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Confidence: " + confidence + "\tValue: " + value;
	}
}
