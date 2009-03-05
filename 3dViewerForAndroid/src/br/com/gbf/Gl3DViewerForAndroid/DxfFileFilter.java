package br.com.gbf.Gl3DViewerForAndroid;

import java.io.File;
import java.io.FilenameFilter;

public class DxfFileFilter implements FilenameFilter {

	public boolean accept(File dir, String filename) {
		return filename.endsWith(".dxf");		
	}

}
