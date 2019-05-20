package vn.com.misa.sticker.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import vn.com.misa.sticker.ImageStickerListener;
import vn.com.misa.sticker.R;

public class ImageSticker extends FrameLayout implements View.OnClickListener {
    private ImageView btnFlip, btnRemove, btnScale;
    private SquareImageView ivMain;
    private FrameLayout flContainer;
    long startTime = 0;
    long endTime = 0;
    float mAngle;
    //move
    // For scalling
    private float this_orgX = -1, this_orgY = -1;
    private float scale_orgX = -1, scale_orgY = -1;
    private double scale_orgWidth = -1, scale_orgHeight = -1;
    // For rotating
    private float rotate_orgX = -1, rotate_orgY = -1, rotate_newX = -1, rotate_newY = -1;
    // For moving
    private float move_orgX = -1, move_orgY = -1;
    private double centerX, centerY;
    private final static int SELF_SIZE_DP = 100;

    //interface communication from view to fragment
    private ImageStickerListener mCallback;

    //
    public ImageSticker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ImageSticker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageSticker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCallback(ImageStickerListener callback) {
        mCallback = callback;
    }

    private void init(Context context) {
        try {
            inflate(context, R.layout.sticker, this);
            btnFlip = findViewById(R.id.btnFlip);
            btnRemove = findViewById(R.id.btnRemove);
            btnScale = findViewById(R.id.btnScale);
            ivMain = findViewById(R.id.ivMain);
            flContainer = findViewById(R.id.flSticker);
            btnFlip.setOnClickListener(this);
            btnRemove.setOnClickListener(this);
            this.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            move_orgX = event.getRawX();
                            move_orgY = event.getRawY();
                            startTime = event.getEventTime();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float offsetX = event.getRawX() - move_orgX;
                            float offsetY = event.getRawY() - move_orgY;
                            ImageSticker.this.setX(ImageSticker.this.getX() + offsetX);
                            ImageSticker.this.setY(ImageSticker.this.getY() + offsetY);
                            move_orgX = event.getRawX();
                            move_orgY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            endTime = event.getEventTime();
                            if (endTime - startTime < 200) {
                                if (ivMain.getBackground() != null) {
                                    setControlHidden(true);
                                } else {
                                    setControlHidden(false);
                                }
                            }

                            break;
                    }
                    return true;
                }
            });
            btnScale.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            this_orgX = ImageSticker.this.getX();
                            this_orgY = ImageSticker.this.getY();
                            scale_orgX = event.getRawX();
                            scale_orgY = event.getRawY();
                            scale_orgWidth = ImageSticker.this.getLayoutParams().width;
                            scale_orgHeight = ImageSticker.this.getLayoutParams().height;
                            rotate_orgX = event.getRawX();
                            rotate_orgY = event.getRawY();
                            centerX = ImageSticker.this.getX() +
                                    ((View) ImageSticker.this.getParent()).getX() +
                                    (float) ImageSticker.this.getWidth() / 2;
                            //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
                            int result = 0;
                            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                            if (resourceId > 0) {
                                result = getResources().getDimensionPixelSize(resourceId);
                            }
                            double statusBarHeight = result;
                            centerY = ImageSticker.this.getY() +
                                    ((View) ImageSticker.this.getParent()).getY() +
                                    statusBarHeight +
                                    (float) ImageSticker.this.getHeight() / 2;
//                        startTime = event.getEventTime();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            rotate_newX = event.getRawX();
                            rotate_newY = event.getRawY();
                            double angle_diff = Math.abs(
                                    Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                                            - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;
                            double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
                            double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());
                            int size = convertDpToPixel(SELF_SIZE_DP, getContext());
                            if (length2 > length1
                                    && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                            ) {
                                //scale up
                                double offsetX = Math.abs(event.getRawX() - scale_orgX);
                                double offsetY = Math.abs(event.getRawY() - scale_orgY);
                                double offset = Math.max(offsetX, offsetY);
                                offset = Math.round(offset);
                                ImageSticker.this.getLayoutParams().width += offset;
                                ImageSticker.this.getLayoutParams().height += offset;
                            } else if (length2 < length1
                                    && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                                    && ImageSticker.this.getLayoutParams().width > size / 2
                                    && ImageSticker.this.getLayoutParams().height > size / 2) {
                                //scale down
                                double offsetX = Math.abs(event.getRawX() - scale_orgX);
                                double offsetY = Math.abs(event.getRawY() - scale_orgY);
                                double offset = Math.max(offsetX, offsetY);
                                offset = Math.round(offset);
                                ImageSticker.this.getLayoutParams().width -= offset;
                                ImageSticker.this.getLayoutParams().height -= offset;
                            }
                            //rotate
                            double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
                            //setRotation((float) angle - 45);
                            setRotation((float) angle - 45);
                            mAngle=(float) angle - 45;
                            rotate_orgX = rotate_newX;
                            rotate_orgY = rotate_newY;
                            scale_orgX = event.getRawX();
                            scale_orgY = event.getRawY();
                            postInvalidate();
                            requestLayout();
                            break;
                        case MotionEvent.ACTION_UP:
//                        endTime = event.getEventTime();
//                        if (endTime - startTime < 200) {
//                            stickerInterface.onTextStickerClicked(CustomView.this);
//                        }
                            break;
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public float getYMain(){
        return this.ivMain.getY();
    }
    public float getXMain(){
        return this.ivMain.getX();
    }

    public float getAngle() {
        return mAngle;
    }

    public float getSizeSticker() {
        return this.ivMain.getWidth();
    }

    public void setDrawable(Drawable drawable) {
        this.ivMain.setImageDrawable(drawable);
    }

    public void setResource(int id) {
        this.ivMain.setImageResource(id);
    }

    public void setBitmap(Bitmap bitmap) {
        this.ivMain.setImageBitmap(bitmap);
    }

    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    public void setControlHidden(boolean hidden) {
        if (hidden) {
            ivMain.setBackground(null);
            btnFlip.setVisibility(INVISIBLE);
            btnRemove.setVisibility(INVISIBLE);
            btnScale.setVisibility(INVISIBLE);
        } else {
            ivMain.setBackgroundResource(R.drawable.border);
            btnFlip.setVisibility(VISIBLE);
            btnRemove.setVisibility(VISIBLE);
            btnScale.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFlip:
                ivMain.setRotationY(ivMain.getRotationY() == -180f ? 0f : -180f);
                ivMain.invalidate();
                requestLayout();
                break;
            case R.id.btnRemove:
                if (this.getParent() != null) {
                    ((ViewGroup) this.getParent()).removeView(this);
                    if (mCallback != null)
                        mCallback.onRemove(this);
                }
                break;
        }
    }

}
