package com.aotuman.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LineChartView mLineChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLineChartView = (LineChartView) findViewById(R.id.lineChart);
        //X轴
        String[] xItem = {"4/1","4/2","4/3","4/4","4/5","4/6","4/7","4/8","4/9","4/10","4/11","4/12",
                "4/13","4/14","4/15","4/16","4/17","4/18","4/19","4/20","4/21","4/22","4/23","4/24"
                ,"4/25","4/26","4/27","4/28","4/29","4/30"};
        ArrayList xItemArray = new ArrayList();
        for (int i = 0; i < xItem.length; i++) {
            xItemArray.add(xItem[i]);
        }
        //Y轴
        int[] yItem = {3,7,19,7,20,19,27,8,18,19,21,20,19,20,8,18,19,21,20,22,21,24,26,24,20,22,21,24,26,24};
        ArrayList<Integer> yItemArray = new ArrayList<>();
        for (int i = 0; i < yItem.length; i++) {
            yItemArray.add(yItem[i]);
        }
        int yMax = findMax(yItem);
        mLineChartView.setXItem(xItemArray);
        mLineChartView.setYItem(yItemArray);
        mLineChartView.setMaxYValue(yMax);

    }

    // 获得数组中最大值
    private int findMax(int[] array){
        int max = array[0];
        for(int i=0;i<array.length;i++){
            if(array[i]>max) max=array[i];
        }
        return max;
    }
}
