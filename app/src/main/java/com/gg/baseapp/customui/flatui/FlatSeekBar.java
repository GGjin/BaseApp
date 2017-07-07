package com.gg.baseapp.customui.flatui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.SeekBar;

import com.gg.baseapp.R;


public class FlatSeekBar extends SeekBar implements Attributes.AttributeChangeListener {

    private Attributes attributes;
    private int Tag;
    PaintDrawable thumb;
    Paint paint;
    public FlatSeekBar(Context context) {
        super(context);
        init(null);
    }

    public FlatSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlatSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
    	Tag=0;
		paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(Color.RED);
		paint.setTextSize(20);
        if (attributes == null)
            attributes = new Attributes(this, getResources());

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.fl_FlatSeekBar);

            // getting common attributes
            int customTheme = a.getResourceId(R.styleable.fl_FlatSeekBar_fl_theme, Attributes.DEFAULT_THEME);
            attributes.setThemeSilent(customTheme, getResources());

            attributes.setSize(a.getDimensionPixelSize(R.styleable.fl_FlatSeekBar_fl_size, Attributes.DEFAULT_SIZE_PX));

            a.recycle();
        }

        // setting thumb
        thumb = new PaintDrawable(attributes.getColor(0));
        thumb.setCornerRadius(attributes.getSize() * 9 / 8);
        thumb.setIntrinsicWidth(attributes.getSize() * 9 / 4);
        thumb.setIntrinsicHeight(attributes.getSize() * 9 / 4);
        setThumb(thumb);

        // progress
        PaintDrawable progress = new PaintDrawable(attributes.getColor(1));
        progress.setCornerRadius(attributes.getSize()/3);
        progress.setIntrinsicHeight(attributes.getSize()/3);
        progress.setIntrinsicWidth(attributes.getSize()/3);
        progress.setDither(true);
        ClipDrawable progressClip = new ClipDrawable(progress, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        // secondary progress
        PaintDrawable secondary = new PaintDrawable(attributes.getColor(2));
        secondary.setCornerRadius(attributes.getSize()/3);
        secondary.setIntrinsicHeight(attributes.getSize()/3);
        ClipDrawable secondaryProgressClip = new ClipDrawable(secondary, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        // background
        PaintDrawable background = new PaintDrawable(attributes.getColor(3));
        background.setCornerRadius(attributes.getSize()/3);
        background.setIntrinsicHeight(attributes.getSize()/3);

        // applying drawable
        LayerDrawable ld = (LayerDrawable) getProgressDrawable();
        ld.setDrawableByLayerId(R.id.background, background);
        ld.setDrawableByLayerId(R.id.progress, progressClip);
        ld.setDrawableByLayerId(R.id.secondaryProgress, secondaryProgressClip);
    }

    public void setTag(int tag)
    {
    	Tag=tag;
    }
    
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		Rect rect = thumb.getBounds();
		canvas.drawText(String.valueOf(Tag), (thumb.getIntrinsicWidth() )
				+ rect.left, thumb.getIntrinsicHeight() / 2-24, paint);
		canvas.restore();
	}
    
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public void onThemeChange() {
        init(null);
    }
}
