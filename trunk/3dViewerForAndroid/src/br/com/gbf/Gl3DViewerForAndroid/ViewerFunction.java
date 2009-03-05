package br.com.gbf.Gl3DViewerForAndroid;

public class ViewerFunction {
	public static enum Functions {
		Zoom,
		RotateAngleX,
		RotateAngleY,
		RotateAngleZ
	};
	
	private String[] functionsDescription = {
		"ZOOM",
		"ROTATE ANGLE X",
		"ROTATE ANGLE Y",
		"ROTATE ANGLE Z"
	};
	
	private Functions mFunction;
	
	public ViewerFunction(Functions initialFunction) {
		mFunction = initialFunction;
	}
	
	public Functions getActualFunction() {
		return mFunction;
	}
	
	public String getActualFunctionDescription() {
		return functionsDescription[mFunction.ordinal()];
	}
	
	public void setEqualToNextFunction() {
		int nextFunctionIndex = mFunction.ordinal() + 1;
		if (nextFunctionIndex > Functions.values().length - 1)
			nextFunctionIndex = 0;
		mFunction = Functions.values()[nextFunctionIndex];
	}

	public void setEqualToPreviousFunction() {
		int previousFunctionIndex = mFunction.ordinal() - 1;
		if (previousFunctionIndex < 0)
			previousFunctionIndex = Functions.values().length - 1;
		mFunction = Functions.values()[previousFunctionIndex];
	}
}