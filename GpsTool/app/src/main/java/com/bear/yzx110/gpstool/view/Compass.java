package com.bear.yzx110.gpstool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;

import com.bear.yzx110.gpstool.entity.WxEntity;

import java.util.List;



/**
 * Created by yzx110 on 2018/7/19.
 */

public class Compass extends View {


    //刻度画笔
    private Paint markerPaint;
    //文本画笔
    private Paint textPaint;
    //大圆形画笔
    private Paint circlePaint;
    //第二个小圆
    private Paint twoPaint;
    //第三个小圆
    private Paint threePaint;
    //第四个圆
    private Paint fourPaint;
    //字符串
    private String northString;
    //字符串
    private String eastString;
    //字符串
    private String southString;
    //字符串
    private String westString;
    //文本高度
    private int textHeight;
    //轴
    private float bearing;
    //画十字架
    private Paint tenPaint;

    //卫星
    private Paint satePaint;

    private List<WxEntity> list;

    public void setList(List<WxEntity> list) {
        if (list != null) {
            Log.i("text", "list的值不为空，在Yuan.java" + list.size());
            this.list = list;
            for (WxEntity wx1 : list) {
                Log.i("text", "list的值不为空，在Yuan.java    " + wx1.getElevation() + "    " + wx1.getAzimuth());
            }
        }
    }

    /**
     * @return
     */
    public float getBearing() {

        return bearing;
    }

    /**
     * @param _bearing
     */
    public void setBearing(float _bearing) {
        bearing = _bearing;

    }

    public Compass(Context context) {
        super(context);

        initCompassView();
    }


    public Compass(Context context, AttributeSet attrs) {
        super(context, attrs);

        initCompassView();
    }

    public Compass(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);

        initCompassView();
    }

    protected void initCompassView() {
        setFocusable(true);

        // 东西南北
        northString = "北";
        eastString = "东";
        southString = "南";
        westString = "西";
        // 设置实心圆画笔
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStrokeWidth(0);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置第二个圆画笔
        twoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        twoPaint.setColor(Color.WHITE);
        twoPaint.setStyle(Paint.Style.STROKE);
        //设置第三个圆画笔
        threePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        threePaint.setColor(Color.WHITE);
        threePaint.setStyle(Paint.Style.STROKE);
        //设置第四个圆的画笔
        fourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fourPaint.setColor(Color.WHITE);
        fourPaint.setStyle(Paint.Style.STROKE);

        //设置卫星画笔
        satePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        satePaint.setColor(Color.RED);
        satePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // 设置线条画笔 刻度
        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(Color.RED);

        //设置十字架
        tenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tenPaint.setColor(Color.WHITE);
        // 设置坐标画笔 东西南北 度数
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28);
        // 设置文字高度
        textHeight = (int) textPaint.measureText("yY");


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(d, d);
    }

    /**
     * @param measureSpec
     * @return
     * @category
     */
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

    @Override
    protected void onDraw(Canvas canvas) {
        // 圆心坐标
        int px = getMeasuredWidth() / 2;

        int py = getMeasuredHeight() / 2;

        // 半径 取最小值
        int radius = Math.min(px, py);
        // 画圆
        canvas.drawCircle(px, py, radius, circlePaint);
        canvas.drawCircle(px, py, radius / 4, twoPaint);
        canvas.drawCircle(px, py, radius / 2, threePaint);
        canvas.drawCircle(px, py, 3 * radius / 4, threePaint);
        //画卫星

        if (list != null) {
            for (WxEntity w : list) {
                Log.i("shiYong", w.getAzimuth() + "    " + w.getElevation());
            }
            Log.i("shiYong", "- - - - - - - - - - - - -\n- - - - - - - - - - - - -\n- - - - - - - - - - - - -\n");


            for (WxEntity w : list) {
                //得到仰角
                float elevation = Float.valueOf(w.getElevation());
                //通过仰角，计算出这个卫星应该绘制到离圆心多远的位置，这里用的是角度的比值
                double r2 = radius*((90.0f - elevation) / 90.0f);
                double azimuth = Float.valueOf(w.getAzimuth());
                double radian = degreeToRadian(360-azimuth + 90);

                double x = px + Math.cos(radian) * r2;
                double y = py + Math.sin(radian) * r2;
                //得到卫星图标的半径
                int sr = 18;
                System.out.print("x坐标" + x);
                System.out.print("y坐标" + y);

                canvas.drawCircle((float) (x - sr), (float) (y - sr), sr, satePaint);
            }
        } else {
            Log.i("fuck", "fuck");
        }


        canvas.save();
        canvas.rotate(-bearing, px, py);
        // 东西南北 坐标位置
        int textWidth = (int) textPaint.measureText("W");

        int cardinalX = px - textWidth / 2;

        int cardinalY = py - radius + textHeight;

        //画24个刻度
        for (int i = 0; i < 24; i++) {
            //画刻度
            canvas.drawLine(px, py - radius, px, py - radius + 10, markerPaint);

            canvas.save();
            //移动原点textHeight距离 开始画东西南北以及度数
            canvas.translate(0, textHeight);
            //判断如果满足条件画东西南北
            if (i % 6 == 0) {
                String dirString = "";
                switch (i) {
                    case (0):
                        dirString = northString;
                        break;
                    case (6):
                        dirString = eastString;
                        break;
                    case (12):
                        dirString = southString;
                        break;
                    case (18):
                        dirString = westString;
                        break;
                }
                //画东西南北

                canvas.drawText(dirString, cardinalX, cardinalY, textPaint);

            } else if (i % 1 == 0) {
                String angle = String.valueOf(i * 15);
                float angleTextWidth = textPaint.measureText(angle);
                int angleTextX = (int) (px - angleTextWidth / 2);
                int angleTextY = py - radius + textHeight;
                //画弧度数
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }
            canvas.restore();
            //每隔15度旋转一下
            canvas.rotate(15, px, py);
        }
        //画十字架
        for (int j = 0; j < 24; j++) {
            canvas.drawLine(px, py - radius, px, py, tenPaint);
            //每隔30度旋转一下
            canvas.rotate(30, px, py);
        }
        canvas.restore();

    }
    /**
     * 将角度转换为弧度，以用于三角函数的运算
     *
     * @param degree
     * @return
     */
    private double degreeToRadian(double degree) {
        return (degree * Math.PI) / 180.0d;
    }


}
