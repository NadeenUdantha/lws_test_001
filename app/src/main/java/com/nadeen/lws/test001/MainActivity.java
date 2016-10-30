package com.nadeen.lws.test001;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.service.wallpaper.*;
import android.view.*;
import java.util.*;

public class MainActivity extends WallpaperService 
{
	@Override
	public WallpaperService.Engine onCreateEngine()
	{
		return new TestEngine();
	}
	public class BPoint
	{
        private int x;
        private int y;
        public BPoint(int x, int y)
		{
			this.x = x;
			this.y = y;
        }
	}
	class TestEngine extends Engine
	{
		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable()
		{
			@Override
			public void run()
			{
				draw();
			}
		};
		private Paint paint = new Paint();
		private boolean visible = true;
		private List<BPoint> bp;
		public TestEngine()
		{
			bp = new ArrayList<BPoint>();
			paint.setAntiAlias(true);
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(10f);
			handler.post(drawRunner);
		}

		@Override
		public void onVisibilityChanged(boolean visible)
		{
			this.visible = visible;
			if (visible)
			{
				handler.post(drawRunner);
			}
			else
			{
				handler.removeCallbacks(drawRunner);
				bp.clear();
				draw();
			}
		}
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder)
		{
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}
		@Override
		public void onTouchEvent(MotionEvent event)
		{
			float x = event.getX();
			float y = event.getY();
			BPoint p = new BPoint((int)x,(int)y);
			if(bp.size() >10)
				bp.remove(0);
			else
			if(!bp.contains(p))
				bp.add(p);
			draw();
			super.onTouchEvent(event);
		}
		private void draw()
		{
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null)
				{
					canvas.drawColor(Color.BLACK);
					for(BPoint point : bp)
						canvas.drawCircle(point.x,point.y,20.0f,paint);
				}
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
			handler.removeCallbacks(drawRunner);
		}
	}
}

