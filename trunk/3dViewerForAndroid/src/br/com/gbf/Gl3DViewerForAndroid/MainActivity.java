package br.com.gbf.Gl3DViewerForAndroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    
    private String mDxfFilesPath = "/data/data/com.android.GLTestApp";
    private String mActivityTitle = "3D Viewer For Android";
    private String mDxfFilenameBuffer[];
    private static InputStreamReader dxfStream;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
		setTitle(mActivityTitle.subSequence(0, mActivityTitle.length()));

        setContentView(R.layout.main);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean res =  super.onCreateOptionsMenu(menu);
    	File GLAppDir = new File(mDxfFilesPath);
    	mDxfFilenameBuffer = GLAppDir.list(new DxfFileFilter());
    	for (int i = 0; i < mDxfFilenameBuffer.length; i++)
        	menu.add(0, Menu.FIRST + i, Menu.NONE, mDxfFilenameBuffer[i]);
  
    	return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int dxfIndex = item.getItemId() - 1;

		try {
			FileInputStream fis = new FileInputStream(mDxfFilesPath + "/" + mDxfFilenameBuffer[dxfIndex]);
	   		dxfStream = new InputStreamReader(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

        startActivity(new Intent(this, ProgressBarActivity.class));

		return super.onOptionsItemSelected(item);
    }
    
    public static InputStreamReader getDxfStream() {
    	return dxfStream;
    }
        
}