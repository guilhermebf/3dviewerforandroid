package com.android.GLTestApp.DxfRenderer;

import android.util.Log;

public class DXFFace extends DXFEntity {
    DXFVector v0;
    DXFVector v1;
    DXFVector v2;
    DXFVector v3;
    DXFVector n;       // normal   
	
	DXFFace() { 
    	type = DXFEntityType.Face; 
    }
    
    void CalculateNormal() {
        DXFVector v01 = new DXFVector(v0.x - v1.x, v0.y - v1.y, v0.z - v1.z);
        DXFVector v02 = new DXFVector(v0.x - v2.x, v0.y - v2.y, v0.z - v2.z);
        DXFVector aux = DXFRenderer.Cross(v01, v02);
        n = new DXFVector(aux.x, aux.y, aux.z);
        float mod = (float) Math.sqrt(n.x*n.x + n.y*n.y + n.z*n.z);
        n.x /= mod;
        n.y /= mod;
        n.z /= mod;
    	//Log.d("CalculateNormal", "v01.x: " + v01.x + " v01.y: " + v01.y + " v01.z: " + v01.z +
    	//		                 "\nv02.x: " + v02.x + " v02.y: " + v02.y + " v02.z: " + v02.z);
    			                // "\nv1.x: " + v1.x + " v1.y: " + v1.y + " v1.z: " + v1.z +
    			                // "\nv0.x: " + v2.x + " v2.y: " + v2.y + " v2.z: " + v2.z);

    }
}
