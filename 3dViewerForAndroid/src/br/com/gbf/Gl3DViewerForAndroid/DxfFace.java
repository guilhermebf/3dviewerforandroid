package br.com.gbf.Gl3DViewerForAndroid;

public class DxfFace extends DxfEntity {
    DxfVector v0;
    DxfVector v1;
    DxfVector v2;
    DxfVector v3;
    DxfVector n;       // normal   
	
	DxfFace() { 
    	type = DxfEntityType.Face; 
    }
    
    void CalculateNormal() {
        DxfVector v01 = new DxfVector(v0.x - v1.x, v0.y - v1.y, v0.z - v1.z);
        DxfVector v02 = new DxfVector(v0.x - v2.x, v0.y - v2.y, v0.z - v2.z);
        DxfVector aux = DxfRenderer.Cross(v01, v02);
        n = new DxfVector(aux.x, aux.y, aux.z);
        float mod = (float) Math.sqrt(n.x*n.x + n.y*n.y + n.z*n.z);
        n.x /= mod;
        n.y /= mod;
        n.z /= mod;
     }
}
