package com.android.GLTestApp.DxfRenderer;

public class DXFLine extends DXFEntity {
    DXFLine() { 
    	type = DXFEntityType.Line; 
    }
    
    DXFVector v0;
    DXFVector v1;
}
