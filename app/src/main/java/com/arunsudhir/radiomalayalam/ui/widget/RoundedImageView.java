package com.arunsudhir.radiomalayalam.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.google.common.base.Optional;

/**
 * A class that 'rounds' the image to be rendered.
 */
public class RoundedImageView extends ImageView {
    private boolean isDirty = true;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void markDirty() {
        this.isDirty = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isDirty) {
            Optional<Bitmap> bitmap = getBitmapCopy();
            if (bitmap.isPresent()) {
                Bitmap roundedImage = getRoundedBitmap(bitmap.get());
                setImageDrawable(new BitmapDrawable(getResources(), roundedImage));
            }
            isDirty = false;
        }
        super.onDraw(canvas);
    }

    private Optional<Bitmap> getBitmapCopy() {
        Optional<Drawable> drawable = getValidatedDrawable();
        if (!drawable.isPresent()) {
            return Optional.absent();
        }
        Bitmap result = ((BitmapDrawable)drawable.get()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        return Optional.of(result);
    }

    private Optional<Drawable> getValidatedDrawable() {
        if (getWidth() == 0 && getHeight() == 0) {
            return Optional.absent();
        }
        return Optional.fromNullable(getDrawable());
    }

    private Bitmap getRoundedBitmap(final Bitmap bitmap) {
        final int radius = getWidth();
        final Bitmap scaledBitmap = scaleBitmapToRadius(bitmap, radius);
        final Bitmap roundedBitmap = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(roundedBitmap);
        final float centerX = radius * 0.5f + 0.7f;
        final float centerY = centerX;
        final float outlineRadius = radius * 0.5f + 0.1f;
        canvas.drawARGB(0, 0, 0, 0);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(centerX, centerY, outlineRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final Rect rect = new Rect(0, 0, radius, radius);
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);
        return roundedBitmap;
    }

    private Bitmap scaleBitmapToRadius(Bitmap bitmap, int radius) {
        if (bitmap.getWidth() == radius && bitmap.getHeight() == radius) {
            return bitmap;
        }
        return Bitmap.createScaledBitmap(bitmap, radius, radius, false);
    }
}
