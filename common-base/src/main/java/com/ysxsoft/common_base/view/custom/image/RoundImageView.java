package com.ysxsoft.common_base.view.custom.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.ysxsoft.common_base.R;

public class RoundImageView extends AppCompatImageView {
	private int width;
	private int height;
	private int defaultRadius = 0;
	private int radius;
	private int leftTopRadius;
	private int rightTopRadius;
	private int rightBottomRadius;
	private int leftBottomRadius;

	public RoundImageView(Context context) {
		this(context, null);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		if (Build.VERSION.SDK_INT < 18) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		// 读取配置
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
		radius = array.getDimensionPixelSize(R.styleable.RoundImageView_radius, defaultRadius);
		leftTopRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_leftRadius, defaultRadius);
		rightTopRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_rightRadius, defaultRadius);
		rightBottomRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_rightBottomRadius, defaultRadius);
		leftBottomRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_leftBottomRadius, defaultRadius);

		//如果四个角的值没有设置，那么就使用通用的radius的值。
		if (defaultRadius == leftTopRadius) {
			leftTopRadius = radius;
		}
		if (defaultRadius == rightTopRadius) {
			rightTopRadius = radius;
		}
		if (defaultRadius == rightBottomRadius) {
			rightBottomRadius = radius;
		}
		if (defaultRadius == leftBottomRadius) {
			leftBottomRadius = radius;
		}
		array.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
		int maxLeft = Math.max(leftTopRadius, leftBottomRadius);
		int maxRight = Math.max(rightTopRadius, rightBottomRadius);
		int minWidth = maxLeft + maxRight;
		int maxTop = Math.max(leftTopRadius, rightTopRadius);
		int maxBottom = Math.max(leftBottomRadius, rightBottomRadius);
		int minHeight = maxTop + maxBottom;
//		if (width >= minWidth && height > minHeight) {
			Path path = new Path();
			//四个角：右上，右下，左下，左上
			path.moveTo(leftTopRadius, 0);
			path.lineTo(width - rightTopRadius, 0);
			path.quadTo(width, 0, width, rightTopRadius);

			path.lineTo(width, height - rightBottomRadius);
			path.quadTo(width, height, width - rightBottomRadius, height);

			path.lineTo(leftBottomRadius, height);
			path.quadTo(0, height, 0, height - leftBottomRadius);

			path.lineTo(0, leftTopRadius);
			path.quadTo(0, 0, leftTopRadius, 0);
			canvas.clipPath(path);

//		}
		super.onDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
	}
}
