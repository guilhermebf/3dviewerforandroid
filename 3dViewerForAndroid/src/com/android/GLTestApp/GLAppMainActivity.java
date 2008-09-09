package com.android.GLTestApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import com.android.GLTestApp.DxfRenderer.DXFRenderer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.Menu.Item;

public class GLAppMainActivity extends Activity {
	public static final int FIRST_ID = Menu.FIRST;
	public static final int SECOND_ID = FIRST_ID+1;
	public static final int THIRD_ID = SECOND_ID+1;
	public static final int FOURTH_ID = THIRD_ID+1;
	public static final int FIFTH_ID = FOURTH_ID+1;
	public static final int SIXTH_ID = FIFTH_ID+1;
	public static final int SEVENTH_ID = SIXTH_ID+1;
	public static final int EIGHTH_ID = SEVENTH_ID+1;
	public static final int NINTH_ID = EIGHTH_ID+1;
	public static final int TENTH_ID = NINTH_ID+1;
	
    public static final int ACTIVITY_VIEW = 0;
    public static final int ELEVENTH_ID = TENTH_ID+1;
	
    public static final String GL_DRAW = "GL_DRAW";
    
    private static String dxfFiles[];
    private static String selectedDxfFilename;
    private static InputStreamReader dxf;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean res =  super.onCreateOptionsMenu(menu);
    	File GLAppDir = new File("/data/data/com.android.GLTestApp");
    	dxfFiles = GLAppDir.list(new DxfFilter());
    	for (int i = 0; i < dxfFiles.length; i++)
    		menu.add(0, FIRST_ID + i, dxfFiles[i]);
 
    	return res;
    }

    @Override
    public boolean onOptionsItemSelected(Item item) {
    	try {
			dxf = new InputStreamReader(new FileInputStream("/data/data/com.android.GLTestApp/" + dxfFiles[item.getId() - 1]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        startSubActivity(new Intent(this, GLAppChooseActivity.class), ACTIVITY_VIEW);
    	return super.onOptionsItemSelected(item);
    }
    
    public static InputStreamReader getDxfStream() {
    	return dxf;
    }
        
}