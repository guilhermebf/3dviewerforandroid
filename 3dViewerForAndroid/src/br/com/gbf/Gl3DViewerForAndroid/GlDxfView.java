package br.com.gbf.Gl3DViewerForAndroid;

import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import br.com.gbf.Gl3DViewerForAndroid.ViewerFunction.Functions;


public class GlDxfView extends GlBaseView {
    private float mRotateAngleX;
    private float mRotateAngleY;
    private float mRotateAngleZ;
    private float mMaxRotateAngleX; // max rotate x angle without releasing the mouse
    private float mMaxRotateAngleY; // max rotate y angle without releasing the mouse
    private float mZoom;
    private float mOldX, mOldY, mActualX, mActualY, mDeltaX, mDeltaY;
    private boolean mRotate;
    private ViewerFunction mViewerFunc;
    private FloatBuffer mFaceBuffer;
    private FloatBuffer mLineBuffer;
    private FloatBuffer mColorBuffer;
    private FloatBuffer mNormalBuffer;
    private DxfRenderer mRenderer;
    
	private BitmapFont font, mOptionFont;
    
	public GlDxfView(Context c) {
		super(c, 20);
		setBackgroundColor(Color.TRANSPARENT);
 		setFocusable(true);
 				
		mRenderer = new DxfRenderer();

		// Initial rotate angles
		mRotate = false;
		mRotateAngleX = 0.0f; mMaxRotateAngleX = 90.0f;
		mRotateAngleY = 0.0f; mMaxRotateAngleY = 90.0f;
		mRotateAngleZ = 0.0f;
		
		// Initial "zoom"
		mZoom = -20.0f;
		
		// Initial option
		mViewerFunc = new ViewerFunction(Functions.Zoom);
		
	}
		
	public void loadDxfFile (InputStreamReader dxf) {
	    mRenderer.Load(dxf);

	    // "Render" (load all triangles) before real render with opengl
		mRenderer.RenderToBuffer();

	    mFaceBuffer   = makeFloatBuffer(mRenderer.getFaceArray());		
		mNormalBuffer = makeFloatBuffer(mRenderer.getNormalArray());			
		mLineBuffer   = makeFloatBuffer(mRenderer.getLineArray());
		mColorBuffer  = makeFloatBuffer(mRenderer.getColorArray());
	}
	
	protected void init(GL10 gl) {
    	FloatBuffer light0_pos   = makeFloatBuffer(new float[] { -50.0f, 50.0f, 0.0f, 0.0f });
      	FloatBuffer light0_color = makeFloatBuffer(new float[] {   0.6f,  0.6f, 0.6f, 1.0f });
     	FloatBuffer light1_pos   = makeFloatBuffer(new float[] {  50.0f, 50.0f, 0.0f, 0.0f });
     	FloatBuffer light1_color = makeFloatBuffer(new float[] {   0.4f,  0.4f, 1.0f, 1.0f });

    	font = new BitmapFont(loadTexture(gl, R.drawable.font2_sem_alpha),0f, -12f, -13f);
    	mOptionFont = new BitmapFont(loadTexture(gl, R.drawable.font2_sem_alpha), 0f, 12f, -13f);

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

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] { 0.3f, 0.4f, 0.6f, 1.0f }, 0);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);
                
        loadDxfFile(MainActivity.getDxfStream());
        ProgressBarActivity.progressBar.dismiss();
	}

	protected void drawFrame(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_LIGHT1);
        gl.glEnable(GL10.GL_LIGHTING);

		// Clear 
        gl.glClearColor( 0.3f, 0.4f, 0.6f, 1.0f );
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // Transformations
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, mZoom);
        gl.glRotatef(mRotateAngleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(mRotateAngleY, 0.0f, 1.0f, 0.0f);        
        gl.glRotatef(mRotateAngleZ, 0.0f, 0.0f, 1.0f);

        // Draw loaded DXF
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
        
        // Necessary to draw strings
        gl.glDisable(GL10.GL_LIGHT0);
        gl.glDisable(GL10.GL_LIGHT1);
        gl.glDisable(GL10.GL_LIGHTING);
        
        // Draw string with angles' values
        gl.glLoadIdentity();
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    	gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glTranslatef(0, 0, -20.0f);
		font.draw(gl,"X=" + Integer.toString(Math.round(mRotateAngleX)) + " Y=" + Integer.toString(Math.round(mRotateAngleY)) + " Z=" + Integer.toString(Math.round(mRotateAngleZ)));
		
		// Draw string with option choosed
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -20.0f); 
		mOptionFont.draw(gl, mViewerFunc.getActualFunctionDescription());
		
		// Necessary to redraw DXF
        gl.glDisable(GL10.GL_TEXTURE_2D); 
		
 		// Flush
        gl.glFlush();
        
        // Automatic rotate mode
        if (mRotate) {
        	mRotateAngleX += 1.0f;
        	mRotateAngleY += 1.0f;
			mRotateAngleZ += 1.0f;			
        }       
	}
		
	@Override 
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				mOldX = event.getX();
				mOldY = event.getY();
				break;
			
			case MotionEvent.ACTION_MOVE:
				mActualX = event.getX();
				mActualY = event.getY();

				// Calculate delta and normalize
				mDeltaX = (mActualX - mOldX)/getWidth();
				mDeltaY = (mActualY - mOldY)/getHeight();

				mRotateAngleX += mDeltaY*mMaxRotateAngleY;	
				mRotateAngleY += mDeltaX*mMaxRotateAngleX;				

				mOldX = mActualX;
				mOldY = mActualY;
				break;
		}
		return true;
	}	
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				if (mViewerFunc.getActualFunction() == Functions.Zoom)
					mZoom++;
				else if (mViewerFunc.getActualFunction() == Functions.RotateAngleX)
					mRotateAngleX++;
				else if (mViewerFunc.getActualFunction() == Functions.RotateAngleY)
					mRotateAngleY++;
				else if (mViewerFunc.getActualFunction() == Functions.RotateAngleZ)
					mRotateAngleZ++;				
				break;
			
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (mViewerFunc.getActualFunction() == Functions.Zoom)
					mZoom--;
				else if (mViewerFunc.getActualFunction() == Functions.RotateAngleX)
					mRotateAngleX--;
				else if (mViewerFunc.getActualFunction() == Functions.RotateAngleY)
					mRotateAngleY--;
				else if (mViewerFunc.getActualFunction() == Functions.RotateAngleZ)
					mRotateAngleZ--;
				break;
			
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mViewerFunc.setEqualToPreviousFunction();
				break;
			
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				mViewerFunc.setEqualToNextFunction();
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
