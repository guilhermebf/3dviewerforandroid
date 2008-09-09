package com.android.GLTestApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class GLTestApp extends Activity {
//	GLSurfaceView mView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        // Make sure to create a TRANSLUCENT window. This is required
        // for SurfaceView to work. Eventually this'll be done by
        // the system automatically.
        //getWindow().setFormat(PixelFormat.TRANSLUCENT);
    
        // We don't need a title either.
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Antigo
        //mView = new GLSurfaceView(this);
        //setContentView(mView);
        
        View v = new GLDXFView(this);
        setContentView(v);
        //mView.setClient(this);

        //setContentView(R.layout.main);
    }
    
    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
    }

}