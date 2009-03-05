package br.com.gbf.Gl3DViewerForAndroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class ProgressBarActivity extends Activity {

	public static ProgressDialog progressBar;
	private String mActivityTitle = "3D Viewer For Android";
	
    @Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setTitle(mActivityTitle.subSequence(0, mActivityTitle.length()));
		progressBar = ProgressDialog.show(ProgressBarActivity.this, "Indeterminate", "Please wait while loading...", true, true);
		setContentView(new GlDxfView(this));
	}
}
