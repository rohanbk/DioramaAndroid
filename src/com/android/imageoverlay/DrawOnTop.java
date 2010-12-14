package com.android.imageoverlay;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;

/**
 * 
 * @author Rohan Balakrishnan
 *
 */

public class DrawOnTop extends View {

	/*
	 * CameraHAngle => Horizontal Camera Angle
	 * Deviation => Change in Compass direction after shifting from portrait to Landscape mode
	 * CicleColor and TextColor => Colors used to fill POI objects and their descriptions
	 * Testing => Map structure to hold POI objects
	 */
	
	
	private static final int CameraHAngle = 90;
	// Deviation in the Compass Direction moving from portrait to landscape mode
	private static final int Deviation = 90;
	private static float Screenwidth;
	private static float AspectRatio;

	private Paint circlepaint;
	private Paint textpaint;
	private static float x;
	private static float y;
	private static int radius;
	private static float Direction;
	private static int CircleColor = Color.YELLOW;
	private static int TextColor = Color.RED;
	private static Map<String, Victims> Testing;
	
	private static final String OnDraw = "OnDraw Method";
	
	public static boolean ChangeInModeToLandscape;
	public static boolean ChangeInModeToPortrait;


	public DrawOnTop(Context context, Map<String, Victims> POI) {
		super(context);
		circlepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlepaint.setStyle(Style.FILL_AND_STROKE);
		textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textpaint.setStyle(Style.FILL_AND_STROKE);
		circlepaint.setColor(CircleColor);
		textpaint.setColor(TextColor);

		Testing = new HashMap<String, Victims>();
		// Log.d("TestingSize",String.valueOf(Testing.size()));
		if (!(Testing.isEmpty())) {
			Testing.clear();
		}

		for (Map.Entry<String, Victims> e : POI.entrySet()) {
			String keyvalue = e.getKey();
			Victims v = e.getValue();
			DrawOnTop.Testing.put(keyvalue, v);
		}

	}

	public DrawOnTop(Context context) {
		super(context);
		circlepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlepaint.setStyle(Style.FILL_AND_STROKE);
		textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textpaint.setStyle(Style.FILL_AND_STROKE);
		circlepaint.setColor(CircleColor);
		textpaint.setColor(TextColor);
		Testing = new HashMap<String, Victims>();

	}

	public DrawOnTop(Context ctx, AttributeSet attr, int res) {
		super(ctx, attr, res);
		circlepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlepaint.setStyle(Style.FILL_AND_STROKE);
		textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textpaint.setStyle(Style.FILL_AND_STROKE);
		circlepaint.setColor(CircleColor);
		textpaint.setColor(TextColor);
		Testing = new HashMap<String, Victims>();

	}

	public DrawOnTop(Context ctx, AttributeSet attr) {
		super(ctx, attr);
		circlepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlepaint.setStyle(Style.FILL_AND_STROKE);
		textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textpaint.setStyle(Style.FILL_AND_STROKE);
		circlepaint.setColor(CircleColor);
		textpaint.setColor(TextColor);
		Testing = new HashMap<String, Victims>();
	}

	public DrawOnTop(Context context, float x, float y, int radius) {
		super(context);
		circlepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlepaint.setStyle(Style.FILL_AND_STROKE);
		textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textpaint.setStyle(Style.FILL_AND_STROKE);
		circlepaint.setColor(CircleColor);
		textpaint.setColor(TextColor);
		Testing = new HashMap<String, Victims>();
		}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredheight = measure(heightMeasureSpec);
		int measuredwidth = measure(widthMeasureSpec);
		setMeasuredDimension(measuredwidth, measuredheight);
	}

	private int measure(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = 200;

		} else {
			result = specSize;
		}
		return result;
	}

	/**
	 * Called when DrawOnTop objects are passed to addcontentview method in Connector Class
	 */
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			super.onDraw(canvas);
			float width = getMeasuredWidth();
			float height = getMeasuredHeight();

			if (!(Testing.isEmpty())) {
				for (Map.Entry<String, Victims> e : Testing.entrySet()) {
					float TranslationAmount, x, y, radius;
					Victims v = e.getValue();
					TranslationAmount = getTranslation(v);
					x = (width / 2) + TranslationAmount;
					if (ChangeInModeToLandscape) {
						y = v.getY() / AspectRatio;
					} else {
						y = v.getY();
					}
					radius = v.getRad();
					canvas.drawCircle(x, y, radius, circlepaint);
					canvas.drawText(v.getDescription(), x + 10, y + 10,
							textpaint);
					canvas.drawText("Dist: "+String.valueOf((int) v.getDistance()), x + 10, y + 20,
							textpaint);

				}
	//			Testing.clear();
			}

			canvas.save();
			canvas.restore();

		} catch (Exception e) {
			// TODO: handle exception
			Log.e(OnDraw, e.getMessage());
		}

	}

	public float Calibration(float width) {
		return (width / CameraHAngle);
	}

	public float getTranslation(Victims v) {
		float updatewidth = DrawOnTop.getwidth();
		float TranslationAmount;
		float FinalDifference = v.getAngle_Offset();

		float MultiplyingFactor = Calibration(updatewidth);
		/*
		 * // This depends on how interpretation of negative and positive angle
		 * // i.e. here Its calculated as POI angle - Phone Angle. This is
		 * rohan's method if (FinalDifference < 0) {
		 * 
		 * TranslationAmount = -(Math.abs(FinalDifference) * MultiplyingFactor);
		 * 
		 * 
		 * } else { TranslationAmount = (Math.abs(FinalDifference) *
		 * MultiplyingFactor);
		 * 
		 * }
		 */

		/*
		 *  Here Angle difference is calculated as Phone Angle - POI angle.
		 *  This is Rohan Balakrishnan's method 
		 */
		
		if (FinalDifference < 0) {

			TranslationAmount = (Math.abs(FinalDifference) * MultiplyingFactor);

		} else {
			TranslationAmount = -(Math.abs(FinalDifference) * MultiplyingFactor);

		}
		return TranslationAmount;
	}
	
	public static void setAspectRatio(float width, float height) {
		DrawOnTop.AspectRatio = (width / height);
	}

	

	public static float getwidth() {
		return DrawOnTop.Screenwidth;
	}

	public static void setwidth(float width) {
		DrawOnTop.Screenwidth = width;
	}

	public static float getDirection() {
		float tempDirection = DrawOnTop.Direction;

		if (ChangeInModeToLandscape) {

			if (tempDirection > 269) {
				tempDirection = tempDirection - 360 + 90;
			} else {
				tempDirection = tempDirection + DrawOnTop.Deviation;
			}

		}
		return tempDirection;
	}

	public static void setDirection(float direction) {
		DrawOnTop.Direction = direction;
	}


}
