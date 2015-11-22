/*
 * Copyright 2015 xuefengyang(https://github.com/xuefengyang)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xuefengyang.tipimageview;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.xuefengyang.tipimageview.R;

public  class TipImageView extends ImageView {

    private Paint  mPaint;
    private Path   mPath;
    private Path   mTextPath;
    private int    mDistanceToEdge;
    private String mTipText;
    private float  mOffsetX;
    private float  mOffsetY;

    private int    mTipBgColor;
    private int    mTipTextColor;
    private int    mTipTextSize;
    private int    mDirection;

    private final static int LEFT_TOP     =1;
    private final static int RIGHT_TOP    =2;
    private final static int LEFT_BOTTOM  =3;
    private final static int RIGHT_BOTTOM =4;
    private final static int DEFAULT_BG_COLOR=Color.RED;
    private final static int DEFAULT_TEXT_COLOR=Color.WHITE;
    private final static int DEFAULT_TEXT_SIZE =22;

    public TipImageView(Context context) {
        this(context, null);
    }
    public TipImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public TipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyle(context,attrs);
        initializePainter();

    }
    private void obtainStyle(Context context,AttributeSet attrs){
        TypedArray ta =context.getTheme().obtainStyledAttributes(attrs,R.styleable.TipImageView,0,0);
        mTipBgColor   =ta.getColor(R.styleable.TipImageView_tipBgColor,DEFAULT_BG_COLOR);
        mTipTextColor =ta.getColor(R.styleable.TipImageView_tipTextColor, DEFAULT_TEXT_COLOR);
        mDirection    =ta.getInt(R.styleable.TipImageView_tipDirection,LEFT_TOP);
        mTipText      =ta.getString(R.styleable.TipImageView_tipText);
        mTipTextSize  =(int)ta.getDimension(R.styleable.TipImageView_tipTextSize,DEFAULT_TEXT_SIZE);
        ta.recycle();
    }
    private void initializePainter(){
        mPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(DEFAULT_TEXT_SIZE);

        mPath =new Path();
        mTextPath =new Path();

    }
    public void setTipText(String tipText){
        mTipText=tipText;
        invalidate();
    }
    public void setTipDirection(int direction){
        mDirection =direction ;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calcPathByDirection();
    }
    private void calcPathByDirection(){
        mDistanceToEdge =Math.min(getMeasuredHeight(),getMeasuredWidth())/7;
        switch (mDirection){
            case LEFT_TOP:
                calcPathLeftTop();
                break;
            case RIGHT_TOP:
                calcPathRightTop();
                break;
            case LEFT_BOTTOM:
                calcPathLeftBottom();
                break;
            case RIGHT_BOTTOM:
                calcPathRightBottom();
                break;
            default:
                calcPathLeftTop();
                break;
        }
        float tipTextWidth  =0;
        float tipTextHeight =0;

        //计算Textpath 的长度
        double pathLength = Math.sqrt(2*(Math.pow(mDistanceToEdge*1.5F,2)));

        //计算文字的width and height
        Rect rect =new Rect();
        mPaint.getTextBounds(mTipText,0,mTipText.length(),rect);
        tipTextHeight =rect.height();
        tipTextWidth =mPaint.measureText(mTipText);

        Paint.FontMetrics fontMetrics =mPaint.getFontMetrics();

        //计算偏移量
        mOffsetX =((float)pathLength-tipTextWidth)/2;
        mOffsetY =Math.abs((fontMetrics.ascent + fontMetrics.descent)/2);


    }
    private void calcPathLeftTop(){
        mPath.moveTo(0, mDistanceToEdge);
        mPath.lineTo(0, mDistanceToEdge * 2);
        mPath.lineTo(mDistanceToEdge * 2, 0);
        mPath.lineTo(mDistanceToEdge,0);
        mPath.close();

        mTextPath.moveTo(0, mDistanceToEdge*1.5F);
        mTextPath.lineTo(mDistanceToEdge*1.5F, 0);

    }
    private void calcPathRightTop(){
        mPath.moveTo(getMeasuredWidth()-mDistanceToEdge*2, 0);
        mPath.lineTo(getMeasuredWidth()-mDistanceToEdge, 0);
        mPath.lineTo(getMeasuredWidth(), mDistanceToEdge);
        mPath.lineTo(getMeasuredWidth(), 2*mDistanceToEdge);
        mPath.close();

        mTextPath.moveTo(getMeasuredWidth()-mDistanceToEdge*1.5F, 0);
        mTextPath.lineTo(getMeasuredWidth(), mDistanceToEdge*1.5F);

    }
    private void calcPathLeftBottom(){
        mPath.moveTo(0, getMeasuredHeight()-mDistanceToEdge*2);
        mPath.lineTo(0, getMeasuredHeight() - mDistanceToEdge);
        mPath.lineTo(mDistanceToEdge, getMeasuredHeight());
        mPath.lineTo(mDistanceToEdge*2,getMeasuredHeight());
        mPath.close();

        mTextPath.moveTo(0, getMeasuredHeight()-mDistanceToEdge*1.5F);
        mTextPath.lineTo(mDistanceToEdge*1.5F, getMeasuredHeight());

    }
    private void calcPathRightBottom(){

        mPath.moveTo(getMeasuredWidth() - mDistanceToEdge * 2, getMeasuredHeight());
        mPath.lineTo(getMeasuredWidth() - mDistanceToEdge, getMeasuredHeight());
        mPath.lineTo(getMeasuredWidth(), getMeasuredHeight() - mDistanceToEdge);
        mPath.lineTo(getMeasuredWidth(), getMeasuredHeight() - mDistanceToEdge * 2);
        mPath.close();

        mTextPath.moveTo(getMeasuredWidth()-mDistanceToEdge*1.5F, getMeasuredHeight());
        mTextPath.lineTo(getMeasuredWidth(), getMeasuredHeight()-mDistanceToEdge*1.5F);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mTipBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mTipTextColor);
        canvas.drawTextOnPath(mTipText,mTextPath,mOffsetX,mOffsetY,mPaint);


    }
}