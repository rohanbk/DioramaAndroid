package com.android.imageoverlay;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.*;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

/**
 * 
 * @author siddhesh
 *
 */


public class Preview extends SurfaceView {

	private Camera mcamera;
	private SurfaceHolder mholder;
	private static final String SurfaceCreated = "SurfaceCreated";
	private static final String SurfaceChanged = "SurfaceChanged";
	private static final String SurfaceDestroyed = "SurfaceDestroyed";

	public Preview(Context context) {
		super(context);
		mholder = getHolder();
		mholder.addCallback(surfaceHolderListener);
		mholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	/*
	 * surfaceHolderListener takes care of changes taking place in camera view
	 */
	
	SurfaceHolder.Callback surfaceHolderListener = new SurfaceHolder.Callback() {

		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			try {
				mcamera.stopPreview();
				mcamera.release();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(SurfaceDestroyed, e.getMessage());
			}

		}

		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			try {
				mcamera = android.hardware.Camera.open();
				mcamera.setPreviewDisplay(holder);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(SurfaceCreated, e.getMessage());
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			try {
				Parameters para = mcamera.getParameters();
				para.setPreviewSize(para.getPreviewSize().width, para
						.getPreviewSize().height);
				mcamera.setParameters(para);
				mcamera.startPreview();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(SurfaceChanged, e.getMessage());
			}

		}
	};

}