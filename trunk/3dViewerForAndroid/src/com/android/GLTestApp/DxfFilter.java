package com.android.GLTestApp;

import java.io.File;
import java.io.FilenameFilter;

public class DxfFilter implements FilenameFilter {

	public boolean accept(File dir, String filename) {
		// TODO Auto-generated method stub
		int i = 0;
		i++;
		return filename.endsWith(".dxf");		
	}

}
