package zhoujie.zi.test;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;

import org.achartengine.ChartFactory;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;

import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;



public class MainActivity extends AppCompatActivity {

    private LinearLayout chart;
    private XYMultipleSeriesRenderer xsr;
    private XYMultipleSeriesDataset msd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chart = (LinearLayout) findViewById(R.id.chart);
        lineView();

    }



    public void lineView(){
        //第一条线
        XYMultipleSeriesDataset msd = new XYMultipleSeriesDataset();
        XYSeries series1 = new XYSeries("当天最高温");
        series1.add(1,30);
        series1.add(2,32);
        series1.add(3,28);
        series1.add(4,25);
        series1.add(5,22);
        series1.add(6,35);
        series1.add(7,29);
        msd.addSeries(series1);
        //第二条线
        XYSeries series2 = new XYSeries("当天最低温");
        series2.add(1,23);
        series2.add(2,25);
        series2.add(3,20);
        series2.add(4,18);
        series2.add(5,14);
        series2.add(6,20);
        series2.add(7,23);
        msd.addSeries(series2);

        XYMultipleSeriesRenderer xsr = new XYMultipleSeriesRenderer();
        xsr.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        xsr.setPointSize(10f);//点的大小
        xsr.setXAxisMax(8);//x轴最大值
        xsr.setYAxisMax(40);//y轴最大值
        xsr.setYAxisMin(0);//y轴最小值
        xsr.setYLabels(10);//y轴刻度
        xsr.setShowAxes(false);//不显示x轴
        xsr.setPanEnabled(false);//不允许拖动
        xsr.setShowCustomTextGrid(false);//是否显示X轴和Y轴网格.
        xsr.setShowGridX(false);//是否显示x轴网格
        xsr.setShowGridY(false);//是否显示y轴网格
        xsr.setShowLabels(false);//是否显示坐标
        xsr.setShowLegend(false);//是否显示图例
        //第一条线
        XYSeriesRenderer r1= new XYSeriesRenderer();
        r1.setColor(Color.RED);
        r1.setPointStyle(PointStyle.CIRCLE);
        r1.setFillPoints(true);//实心
        r1.setDisplayChartValues(true);//显示点的数值
        r1.setChartValuesSpacing(16);// 显示的点的值与图的距离
        r1.setChartValuesTextSize(30);//数值字体大小
        r1.setLineWidth(3);//设置线宽
        xsr.addSeriesRenderer(r1);
        //第二条线
        XYSeriesRenderer r2= new XYSeriesRenderer();
        r2.setColor(Color.BLUE);
        r2.setPointStyle(PointStyle.CIRCLE);
        r2.setFillPoints(true);//实心
        r2.setDisplayChartValues(true);//显示点的数值
        r2.setChartValuesSpacing(16);// 显示的点的值与图的距离
        r2.setChartValuesTextSize(30);//数值字体大小
        r2.setLineWidth(3);//设置线宽
        xsr.addSeriesRenderer(r2);


        GraphicalView view = ChartFactory.getLineChartView(this, msd, xsr);
        view.setBackgroundColor(Color.BLACK);
        chart.addView(view);
        //setContentView(view);
    }



    public void buttonT(View view) {
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }
}

