package br.com.gbf.Gl3DViewerForAndroid;

public class DxfLine extends DxfEntity {
    DxfLine() { 
    	type = DxfEntityType.Line; 
    }
    
    DxfVector v0;
    DxfVector v1;
}
