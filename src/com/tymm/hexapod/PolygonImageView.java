package com.tymm.hexapod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PolygonImageView extends ImageView {

	private Polygon poly_bottom_right;
	private Polygon poly_right_right;
	private Polygon poly_top_right;
	private Polygon poly_top_top;
	private Polygon poly_top_left;
	private Polygon poly_left_left;
	private Polygon poly_bottom_left;
	private Polygon poly_bottom_bottom;

	private float width;
	private float height;
	private float offset_y = 0;
	private float offset_x = 0;

	private PointF p_a;
	private PointF p_b;
	private PointF p_c;
	private PointF p_d;
	private PointF p_e;
	private PointF p_f;
	private PointF p_g;
	private PointF p_h;
	private PointF p_i;
	private PointF p_j;
	private PointF p_k;
	private PointF p_l;
	private PointF p_m;
	private PointF p_n;
	private PointF p_o;
	private PointF p_p;

	private Paint mPaint;

	public PolygonImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/** ImageView.getWidth() and ImageView.getHeight() return wrong values when Android scales the ImageView to fit the screen */
		// Get image matrix values and place them in an array
		float[] f = new float[9];
		getImageMatrix().getValues(f);

		// Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
		final float scaleX = f[Matrix.MSCALE_X];
		final float scaleY = f[Matrix.MSCALE_Y];

		// Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
		final Drawable d = getDrawable();
		final int origW = d.getIntrinsicWidth();
		final int origH = d.getIntrinsicHeight();

		// Calculate the actual dimensions
		this.width = Math.round(origW * scaleX);
		this.height = Math.round(origH * scaleY);

		/** Create hit boxes */
		// Create bottom right hit zone
		p_a = new PointF(offset_x+width-(width/4)-(width/16), offset_y+height);
		p_b = new PointF(offset_x+width-(width/4)-(width/16), offset_y+height-(height/4)-(height/16));
		p_c = new PointF(offset_x+width, offset_y+height-(height/4)-(height/16));
		p_d = new PointF(offset_x+width, offset_y+height);

		PointF[] points_b_r = {p_a, p_b, p_c, p_d};
		this.poly_bottom_right = new Polygon(points_b_r, 4);

		// Create right right hit zone
		p_e = new PointF(offset_x+width, offset_y+(height/4)+(height/16));
		p_f = new PointF(offset_x+width-(width/4)-(width/16), offset_y+(height/4)+(height/16));

		PointF[] points_r_r = {p_b, p_c, p_e, p_f};
		this.poly_right_right = new Polygon(points_r_r, 4);

		// Create top right hit zone
		p_g = new PointF(offset_x+width-(width/4)-(width/16), offset_y);
		p_h = new PointF(offset_x+width, offset_y);

		PointF[] points_t_r = {p_f, p_e, p_h, p_g};
		this.poly_top_right= new Polygon(points_t_r, 4);

		// Create top top hit zone
		p_i = new PointF(offset_x+(width/4)+(width/16), offset_y);
		p_j = new PointF(offset_x+(width/4)+(width/16), offset_y+(height/4)+(height/16));

		PointF[] points_t_t = {p_f, p_g, p_i, p_j};
		this.poly_top_top = new Polygon(points_t_t, 4);

		// Create top left hit zone
		p_k = new PointF(offset_x, offset_y+(height/4)+(height/16));
		p_l = new PointF(offset_x, offset_y);

		PointF[] points_t_l = {p_i, p_j, p_k, p_l};
		this.poly_top_left = new Polygon(points_t_l, 4);

		// Create left left hit zone
		p_m = new PointF(offset_x, offset_y+height-(height/4)-(height/16));
		p_n = new PointF(offset_x+(width/4)+(width/16), offset_y+height-(height/4)-(height/16));

		PointF[] points_l_l = {p_k, p_j, p_n, p_m};
		this.poly_left_left = new Polygon(points_l_l, 4);

		// Create bottom left hit zone
		p_o = new PointF(offset_x, offset_y+height);
		p_p = new PointF(offset_x+(width/4)+(width/16), offset_y+height);

		PointF[] points_b_l = {p_m, p_n, p_p, p_o};
		this.poly_bottom_left = new Polygon(points_b_l, 4);

		// Create bottom bottom hit zone
		PointF[] points_b_b = {p_n, p_p, p_a, p_b};
		this.poly_bottom_bottom = new Polygon(points_b_b, 4);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Right right
		canvas.drawLine(p_b.x, p_b.y, p_c.x, p_c.y, mPaint);
		canvas.drawLine(p_c.x, p_c.y, p_e.x, p_e.y, mPaint);
		canvas.drawLine(p_e.x, p_e.y, p_f.x, p_f.y, mPaint);
		canvas.drawLine(p_f.x, p_f.y, p_b.x, p_b.y, mPaint);

		// Left left
		canvas.drawLine(p_k.x, p_k.y, p_j.x, p_j.y, mPaint);
		canvas.drawLine(p_j.x, p_j.y, p_n.x, p_n.y, mPaint);
		canvas.drawLine(p_n.x, p_n.y, p_m.x, p_m.y, mPaint);
		canvas.drawLine(p_m.x, p_m.y, p_k.x, p_k.y, mPaint);

		// Bottom right
		canvas.drawLine(p_a.x, p_a.y, p_b.x, p_b.y, mPaint);
		canvas.drawLine(p_b.x, p_b.y, p_c.x, p_c.y, mPaint);
		canvas.drawLine(p_c.x, p_c.y, p_d.x, p_d.y, mPaint);
		canvas.drawLine(p_d.x, p_d.y, p_a.x, p_a.y, mPaint);

		// Top right
		canvas.drawLine(p_f.x, p_f.y, p_e.x, p_e.y, mPaint);
		canvas.drawLine(p_e.x, p_e.y, p_h.x, p_h.y, mPaint);
		canvas.drawLine(p_h.x, p_h.y, p_g.x, p_g.y, mPaint);
		canvas.drawLine(p_g.x, p_g.y, p_f.x, p_f.y, mPaint);

		// Top top
		canvas.drawLine(p_f.x, p_f.y, p_g.x, p_g.y, mPaint);
		canvas.drawLine(p_g.x, p_g.y, p_i.x, p_i.y, mPaint);
		canvas.drawLine(p_i.x, p_i.y, p_j.x, p_j.y, mPaint);
		canvas.drawLine(p_j.x, p_j.y, p_f.x, p_f.y, mPaint);

		// Top left
		canvas.drawLine(p_i.x, p_i.y, p_j.x, p_j.y, mPaint);
		canvas.drawLine(p_j.x, p_j.y, p_k.x, p_k.y, mPaint);
		canvas.drawLine(p_k.x, p_k.y, p_l.x, p_l.y, mPaint);
		canvas.drawLine(p_l.x, p_l.y, p_i.x, p_i.y, mPaint);

		// Bottom left
		canvas.drawLine(p_m.x, p_m.y, p_n.x, p_n.y, mPaint);
		canvas.drawLine(p_n.x, p_n.y, p_p.x, p_p.y, mPaint);
		canvas.drawLine(p_p.x, p_p.y, p_o.x, p_o.y, mPaint);
		canvas.drawLine(p_o.x, p_o.y, p_m.x, p_m.y, mPaint);

		// Bottom bottom
		canvas.drawLine(p_n.x, p_n.y, p_p.x, p_p.y, mPaint);
		canvas.drawLine(p_p.x, p_p.y, p_a.x, p_a.y, mPaint);
		canvas.drawLine(p_a.x, p_a.y, p_b.x, p_b.y, mPaint);
		canvas.drawLine(p_b.x, p_b.y, p_n.x, p_n.y, mPaint);
	}

	public Polygon getAreaBottomRight() {
		return this.poly_bottom_right;
	}

	public Polygon getAreaRightRight() {
		return this.poly_right_right;
	}

	public Polygon getAreaTopRight() {
		return this.poly_top_right;
	}

	public Polygon getAreaTopTop() {
		return this.poly_top_top;
	}

	public Polygon getAreaTopLeft() {
		return this.poly_top_left;
	}

	public Polygon getAreaLeftLeft() {
		return this.poly_left_left;
	}

	public Polygon getAreaBottomLeft() {
		return this.poly_bottom_left;
	}

	public Polygon getAreaBottomBottom() {
		return this.poly_bottom_bottom;
	}
}
