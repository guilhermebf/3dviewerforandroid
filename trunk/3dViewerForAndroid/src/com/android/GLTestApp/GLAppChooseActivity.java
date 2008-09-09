package com.android.GLTestApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class GLAppChooseActivity extends Activity {
//	private GLDXFView v;
//    public static DXFRenderer mRenderer;
	public static ProgressDialog pd;
	
//    protected boolean isFullscreenOpaque() {
//        // Our main window is set to translucent, but we know that we will
//        // fill it with opaque data. Tell the system that so it can perform
//        // some important optimizations.
//        return true;
//    }
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		pd = ProgressDialog.show(GLAppChooseActivity.this, "Indeterminate", "Please wait while loading...", true, true);
		setContentView(new GLDXFView(this));
	}
}
