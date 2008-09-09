package com.android.GLTestApp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.android.GLTestApp.DxfRenderer.DXFRenderer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GLDXFView extends GLTutorialBase {
    //private DXFRenderer mRenderer;
    private float mRotateAngleX;
    private float mMaxRotateAngleX; // max rotate x angle without releasing the mouse
    private float mMaxRotateAngleY; // max rotate y angle without releasing the mouse
    private float mRotateAngleY;
    private float mRotateAngleZ;
    private float mZoom;
    private boolean mRotate;
    private int mOption;
    private int mAngle;
    private FloatBuffer mFaceBuffer;
    private FloatBuffer mLineBuffer;
    private FloatBuffer mColorBuffer;
    private FloatBuffer mNormalBuffer;
    private DXFRenderer mRenderer;
    
	public GLDXFView(Context c) {
		super(c, 500);
		setBackgroundColor(Color.GRAY);
 		setFocusable(true);
		
		mRenderer = new DXFRenderer();

		// Initial rotate angles
		mRotate = false;
		mRotateAngleX = 0.0f; mMaxRotateAngleX = 90.0f;
		mRotateAngleY = 0.0f; mMaxRotateAngleY = 90.0f;
		mRotateAngleZ = 0.0f;
		mAngle = 1;
		
		// Initial "zoom"
		mZoom = -20.0f;
		
		// Initial option
		mOption = Options.Zoom.ordinal();
		
	}
		
	public void loadDxfFile (InputStreamReader dxf) {
//		// path: new InputStreamReader(new FileInputStream("/data/data/com.android.GLTestApp/" + GLAppMainActivity.getSelectedDxfFilename()))
	    mRenderer.Load(dxf);
//		
////		mRenderer.Load(new InputStreamReader(c.getResources().openRawResource(R.raw.penguin)));
////		mRenderer.Load(new InputStreamReader(c.getResources().openRawResource(R.raw.bridge)));
////		mRenderer.Load(new InputStreamReader(c.getResources().openRawResource(R.raw.cube)));
////		mRenderer.Load(new InputStreamReader(c.getResources().openRawResource(R.raw.diamond)));
////		mRenderer.Load(new InputStreamReader(c.getResources().openRawResource(R.raw.cube_test)));
//	
//		// "Render" (load all triangles) before real render with opengl
		mRenderer.RenderToBuffer();
		
		if (mRenderer.getFaceArray().length > 0)
		{
			mFaceBuffer = makeFloatBuffer(mRenderer.getFaceArray());
			Log.i("FaceBuffer", "Capacity=" + mFaceBuffer.capacity());
		}
		
		if (mRenderer.getNormalArray().length > 0)
		{
			mNormalBuffer = makeFloatBuffer(mRenderer.getNormalArray());			
			Log.i("NormalBuffer", "Capacity=" + mNormalBuffer.capacity());
		}
		
		if (mRenderer.getLineArray().length > 0)
		{
			mLineBuffer = makeFloatBuffer(mRenderer.getLineArray());
			Log.i("LineBuffer", "Capacity=" + mLineBuffer.capacity());
		}
		
		if (mRenderer.getColorArray().length > 0)
		{
			mColorBuffer = makeFloatBuffer(mRenderer.getColorArray());
			Log.i("ColorBuffer", "Capacity=" + mColorBuffer.capacity());
		} 
	}
	
	protected void init(GL10 gl) {
    	//float light0_pos[] = new float[] { -50.0f, 50.0f, 0.0f, 0.0f };
    	FloatBuffer light0_pos = makeFloatBuffer(new float[] { -50.0f, 50.0f, 0.0f, 0.0f });
    	 
        // white light
    	//float light0_color[] = { 0.6f, 0.6f, 0.6f, 1.0f };
    	FloatBuffer light0_color = makeFloatBuffer(new float[] { 0.6f, 0.6f, 0.6f, 1.0f });

    	//float light1_pos[] = { 50.0f, 50.0f, 0.0f, 0.0f };  
    	FloatBuffer light1_pos = makeFloatBuffer(new float[] { 50.0f, 50.0f, 0.0f, 0.0f });

        // cold blue light
    	//float light1_color[] = { 0.4f, 0.4f, 1.0f, 1.0f };  
    	FloatBuffer light1_color = makeFloatBuffer(new float[] { 0.4f, 0.4f, 1.0f, 1.0f });

        /* remove back faces */
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        /* speedups */
        gl.glEnable(GL10.GL_DITHER);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_FASTEST);

        /* light */
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0_pos);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE,  light0_color);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, light1_pos);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE,  light1_color);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_LIGHT1);
        gl.glEnable(GL10.GL_LIGHTING);

//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] { 0.3f, 0.4f, 0.6f, 1.0f }, 0);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);
        
        loadDxfFile(GLAppMainActivity.getDxfStream());
        GLAppChooseActivity.pd.dismiss();
        setBackgroundColor(Color.TRANSPARENT);
	}

	protected void drawFrame(GL10 gl) {
		// Clear 
        gl.glClearColor( 0.3f, 0.4f, 0.6f, 1.0f );
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // Transformations
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, mZoom);
        gl.glRotatef(mRotateAngleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(mRotateAngleY, 0.0f, 1.0f, 0.0f);        
        gl.glRotatef(mRotateAngleZ, 0.0f, 0.0f, 1.0f);
        
        // Read triangles in real-time
//        mRenderer.Render(gl);
        
        // Pre-loaded triangles renderer
  
        if (mColorBuffer != null) {
        	gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        	gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        }
        
        if (mNormalBuffer != null) {
        	gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        	gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        }
        
        if (mFaceBuffer != null) {
        	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFaceBuffer);
        	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        	gl.glDrawArrays(GL10.GL_TRIANGLES, 0, mFaceBuffer.capacity() / 3);
        }
        
        if (mLineBuffer != null) {        
        	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mLineBuffer);
        	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        	gl.glDrawArrays(GL10.GL_LINES, 0, mLineBuffer.capacity() / 3);
        }
        
 		// Flush
        gl.glFlush();
        
        if (mRotate) {
        	mRotateAngleX += 1.0f;
        	mRotateAngleY += 1.0f;
			mRotateAngleZ += 1.0f;
        }       
	}
		
	@Override public boolean onTouchEvent(MotionEvent event) {
	    float oldX, oldY, actualX, actualY, deltaX, deltaY;

		switch (event.getAction())
		{
//			case MotionEvent.ACTION_DOWN:
//				oldX = event.getX();
//				oldY = event.getY();
//				break;
			
			case MotionEvent.ACTION_MOVE:
				if (event.getHistorySize() > 0) {
					oldX = event.getHistoricalX(0);
					oldY = event.getHistoricalY(0);

					// Get the point of this event
					actualX = event.getX();
					actualY = event.getY();
	
					// Calculate delta and normalize
					deltaX = (actualX - oldX)/getWidth();
					deltaY = (actualY - oldY)/getHeight();
	
					mRotateAngleY += deltaX*mMaxRotateAngleX;				
					mRotateAngleX += deltaY*mMaxRotateAngleY;
	
					oldX = actualX;
					oldY = actualY;
				}
				break;
		}
		return true;
	}
	
	@Override public boolean onKeyUp(int keyCode, KeyEvent event) {
		mAngle = 1;
		return true;
	}
	
	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {		
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				if (mOption == Options.Zoom.ordinal())
					mZoom += mAngle++;
				else if (mOption == Options.RotateAngleX.ordinal())
					mRotateAngleX += mAngle++;
				else if (mOption == Options.RotateAngleY.ordinal())
					mRotateAngleY += mAngle++;
				else if (mOption == Options.RotateAngleZ.ordinal())
					mRotateAngleZ += mAngle++;				
				break;
			
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (mOption == Options.Zoom.ordinal())
					mZoom -= mAngle++;
				else if (mOption == Options.RotateAngleX.ordinal())
					mRotateAngleX -= mAngle++;
				else if (mOption == Options.RotateAngleY.ordinal())
					mRotateAngleY -= mAngle++;
				else if (mOption == Options.RotateAngleZ.ordinal())
					mRotateAngleZ -= mAngle++;
				break;
			
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mOption--;
				if (mOption < 0)
					mOption = Options.values().length - 1;
				break;
			
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				mOption++;
				if (mOption == Options.values().length)
					mOption = 0;
				break;
				
			case KeyEvent.KEYCODE_DPAD_CENTER:
				mRotate = !mRotate;
				break;
				
			default:
				return false;
		}		
		return true;
	}

}
