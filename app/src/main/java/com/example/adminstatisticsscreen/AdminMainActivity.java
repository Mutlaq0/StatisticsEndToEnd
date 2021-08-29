package com.example.adminstatisticsscreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminMainActivity extends AppCompatActivity {


    BottomNavigationView bottom;
    //TextViews Of percentage
    TextView activitiesText;
    TextView newParentsText;
    TextView newKidsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        activitiesText = (TextView)findViewById(R.id.activities_percentage);
        newParentsText = (TextView)findViewById(R.id.newParents_percentage);
        newKidsText = (TextView)findViewById(R.id.newChildren_percentage);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8020/")
                // when sending data in json format we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // and build our retrofit builder.
                .build();
        // create an instance for our retrofit api class.
       RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        ArrayList<Course> stubRep = StubRepository.getInstance().courses;
        Log.d("Mutlaq    ",stubRep.size()+"");
        // retrieve data
        Call<HashMap<String,Integer>> newKids = retrofitAPI.getNewKids(2);
        Call<HashMap<String,Integer>> newParents = retrofitAPI.getNewParents(2);
        Call<HashMap<String,Double>> activityTime = retrofitAPI.getAcitvityTime(2);
        Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(2);
        //find charts in layout

        PieChart newKidsChart = (PieChart)findViewById(R.id.newChildren);
        PieChart newParentsChart = (PieChart)findViewById(R.id.newParents);
        PieChart activityTimeChart = (PieChart)findViewById(R.id.acticitiesInHours);
        PieChart kidsByCategoryChart = (PieChart)findViewById(R.id.kidsPerCategoryChart);
        BarChart timedKidsByCategoryChart = (BarChart)findViewById(R.id.bar_chart);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);





        //get the data
        bottom = findViewById(R.id.navigation);
        bottom.setSelectedItemId(R.id.homeScreen);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeScreen:
                        return true;
                    case R.id.usersScreen:
                        startActivity(new Intent(AdminMainActivity.this, UsersActivity.class));
                        return true;
                    case R.id.leadersScreen:
                        startActivity(new Intent(AdminMainActivity.this, LeadersActivity.class));
                        return true;
                    case R.id.coursesScreen:
                        startActivity(new Intent(AdminMainActivity.this, CoursesActivity.class));
                        return true;
                    case R.id.moreScreen:
                        startActivity(new Intent(AdminMainActivity.this, MoreActivity.class));
                        return true;
                }
                return false;
            }
        });

        /////////// KIDS CHART CALL
        newKids.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                if(response.isSuccessful() && response.body() !=null){
                    updateChart(response.body(),newKidsChart,0);

                    double newKids = (double) response.body().get("newKids");
                    double totalKids = (double) response.body().get("totalKids");
                    newKidsText.setText( String.format("% .2f",newKids/(newKids+totalKids))+ "%");
                    newKidsText.setBackgroundResource(R.drawable.rounded_textview);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
            }
        });


        /////////// PARENTS CHART CALL
        newParents.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                if(response.isSuccessful() && response.body() !=null){
                    updateChart(response.body(),newParentsChart,1);

                    double newParents = (double) response.body().get("newParents");
                    double totalParents = (double) response.body().get("totalParents");
                    newParentsText.setText( String.format("% .2f",newParents/(newParents+totalParents))+ "%");
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
            }
        });
        /////////// KIDSBYCATEGORY CHART CALL
        kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                if(response.isSuccessful() && response.body() !=null){
                    updateChart(response.body(),kidsByCategoryChart,2);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Log.d("mutlaq","kidsByCategoryChart Faliure");
                Toast.makeText(AdminMainActivity.this, "----kidsByCategoryChart Faliure, Please Do something.---", Toast.LENGTH_SHORT).show();
            }
        });


        /////////// ACTIVITY CHART CALL
        activityTime.enqueue(new Callback<HashMap<String, Double>>() {
            @Override
            public void onResponse(Call<HashMap<String, Double>> call, Response<HashMap<String, Double>> response) {
                if(response.isSuccessful() && response.body() !=null){
                    updateActivitytChart(response.body(),activityTimeChart);

                    double activity = (double) response.body().get("activityTime");
                    double totalActivity = (double) response.body().get("totalTime");
                    activitiesText.setText( String.format("% .2f",activity/(activity+totalActivity))+ "%");
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();

            }
        });

        barChart(timedKidsByCategoryChart,2);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0 ){
                    barChart(timedKidsByCategoryChart,1);
                    Call<HashMap<String,Integer>> newKids = retrofitAPI.getNewKids(1);
                    Call<HashMap<String,Integer>> newParents = retrofitAPI.getNewParents(1);
                    Call<HashMap<String,Double>> activityTime = retrofitAPI.getAcitvityTime(1);
                    Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(1);
                    /////////// KIDS CHART CALL
                    newKids.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),newKidsChart,0);
                                double newKids = (double) response.body().get("newKids");
                                double totalKids = (double) response.body().get("totalKids");
                                newKidsText.setText( String.format("% .2f",newKids/(newKids+totalKids))+ "%");
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });


                    /////////// PARENTS CHART CALL
                    newParents.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),newParentsChart,1);

                                double newParents = (double) response.body().get("newParents");
                                double totalParents = (double) response.body().get("totalParents");
                                newParentsText.setText( String.format("% .2f",newParents/(newParents+totalParents))+ "%");

                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });
                    /////////// KIDSBYCATEGORY CHART CALL
                    kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),kidsByCategoryChart,2);
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Log.d("mutlaq","kidsByCategoryChart Faliure");
                            Toast.makeText(AdminMainActivity.this, "----kidsByCategoryChart Faliure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });


                    /////////// ACTIVITY CHART CALL
                    activityTime.enqueue(new Callback<HashMap<String, Double>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Double>> call, Response<HashMap<String, Double>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateActivitytChart(response.body(),activityTimeChart);

                                double activity = (double) response.body().get("activityTime");
                                double totalActivity = (double) response.body().get("totalTime");
                                activitiesText.setText( String.format("% .2f",activity/(activity+totalActivity))+ "%");
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else if (position == 1 ){
                    barChart(timedKidsByCategoryChart ,2);
                    Call<HashMap<String,Integer>> newKids = retrofitAPI.getNewKids(2);
                    Call<HashMap<String,Integer>> newParents = retrofitAPI.getNewParents(2);
                    Call<HashMap<String,Double>> activityTime = retrofitAPI.getAcitvityTime(2);
                    Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(2);
                    /////////// KIDS CHART CALL
                    newKids.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),newKidsChart,0);
                                double newKids = (double) response.body().get("newKids");
                                double totalKids = (double) response.body().get("totalKids");
                                newKidsText.setText( String.format("% .2f",newKids/(newKids+totalKids))+ "%");
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });


                    /////////// PARENTS CHART CALL
                    newParents.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),newParentsChart,1);
                                double newParents = (double) response.body().get("newParents");
                                double totalParents = (double) response.body().get("totalParents");
                                newParentsText.setText( String.format("% .2f",newParents/(newParents+totalParents))+ "%");
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });
                    /////////// KIDSBYCATEGORY CHART CALL
                    kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),kidsByCategoryChart,2);
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Log.d("mutlaq","kidsByCategoryChart Faliure");
                            Toast.makeText(AdminMainActivity.this, "----kidsByCategoryChart Faliure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });


                    /////////// ACTIVITY CHART CALL
                    activityTime.enqueue(new Callback<HashMap<String, Double>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Double>> call, Response<HashMap<String, Double>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateActivitytChart(response.body(),activityTimeChart);
                                double activity = (double) response.body().get("activityTime");
                                double totalActivity = (double) response.body().get("totalTime");
                                activitiesText.setText( String.format("% .2f",activity/(activity+totalActivity))+ "%");

                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    barChart(timedKidsByCategoryChart,3);

                    Call<HashMap<String,Integer>> newKids = retrofitAPI.getNewKids(3);
                    Call<HashMap<String,Integer>> newParents = retrofitAPI.getNewParents(3);
                    Call<HashMap<String,Double>> activityTime = retrofitAPI.getAcitvityTime(3);
                    Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(3);
                    /////////// KIDS CHART CALL
                    newKids.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),newKidsChart,0);
                                double newKids = (double) response.body().get("newKids");
                                double totalKids = (double) response.body().get("totalKids");
                                newKidsText.setText( String.format("% .2f",newKids/(newKids+totalKids))+ "%");

                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });


                    /////////// PARENTS CHART CALL
                    newParents.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),newParentsChart,1);
                                double newParents = (double) response.body().get("newParents");
                                double totalParents = (double) response.body().get("totalParents");
                                newParentsText.setText( String.format("% .2f",newParents/(newParents+totalParents))+ "%");

                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });
                    /////////// KIDSBYCATEGORY CHART CALL
                    kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateChart(response.body(),kidsByCategoryChart,2);
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                            Log.d("mutlaq","kidsByCategoryChart Faliure");
                            Toast.makeText(AdminMainActivity.this, "----kidsByCategoryChart Faliure, Please Do something.---", Toast.LENGTH_SHORT).show();
                        }
                    });


                    /////////// ACTIVITY CHART CALL
                    activityTime.enqueue(new Callback<HashMap<String, Double>>() {
                        @Override
                        public void onResponse(Call<HashMap<String, Double>> call, Response<HashMap<String, Double>> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                updateActivitytChart(response.body(),activityTimeChart);
                                double activity = (double) response.body().get("activityTime");
                                double totalActivity = (double) response.body().get("totalTime");
                                activitiesText.setText( String.format("% .2f",activity/(activity+totalActivity))+ "%");

                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "----Failure, Please Do something.---", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void updateChart(HashMap<String, Integer> map, PieChart chart, int chartType){
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        int darkBlue = Color.rgb(0,128,255);
        int lightBlue = Color.rgb(0,191,255);
        int aliceBlue = Color.rgb(240,248,255);
        int gold =Color.rgb(255,215,0);
        //initializing data
        Map<String, Integer> typeAmountMap = map;
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(aliceBlue);

        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        if(chartType ==0){
            pieData.setDrawValues(false);
            chart.setHoleRadius(85);
            colors.add(lightBlue);
            chart.setDrawMarkers(false);
            chart.setCenterText(String.valueOf(map.get("newKids")));
            chart.setCenterTextSize(20);
        }else if(chartType ==1){
            pieData.setDrawValues(false);
            colors.add(gold);
            chart.setDrawEntryLabels(false);
            chart.setHoleRadius(85);
            chart.setCenterText(String.valueOf(map.get("newParents")));
            chart.setCenterTextSize(20);
        }else{
            pieData.setValueTextSize(15);
            chart.setDrawHoleEnabled(false);
            colors.add(darkBlue);
            colors.add(lightBlue);
            colors.add(gold);
        }
        chart.setRotationEnabled(false);
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        chart.setDrawEntryLabels(false);
        chart.setData(pieData);
        chart.invalidate();
    }
    private void updateActivitytChart(HashMap<String, Double> map, PieChart chart){
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        chart.setRotationEnabled(false);
        //initializing data
        Map<String, Double> typeAmountMap = map;
        int aliceBlue = Color.rgb(240,248,255);
        int darkBlue = Color.rgb(0,128,255);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(aliceBlue);
        colors.add(darkBlue);
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieDataSet.setDrawValues(false);
     //   pieData.setDrawValues(true);
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        chart.setCenterText(String.valueOf(map.get("activityTime")));
        chart.setCenterTextSize(20);
        chart.setDrawEntryLabels(false);
        chart.setHoleRadius(85);
        chart.setData(pieData);
        chart.invalidate();
    }


    private String[] getXAxisValues(int period) {
        String[] labels;
        if(period ==1) {

            labels = new String[]{"","D1", "D2",  "D3", "D4", "D5","D6", "D7"};
        }else if(period == 2) {
            labels = new String[]{"","W1", "W2", "W3", "W4", "W5"};
        }else {
            labels = new String[]{ "","M1", "M2", "M3", "M4", "M5",
                    "M6", "M7",
                    "M8", " M9", "M10", "M11", "M12"};
        }
        return labels;
    }




    private void barChart( BarChart mChart ,int period) {
        int darkBlue = Color.rgb(0,128,255);
        int lightBlue = Color.rgb(0,191,255);
        int aliceBlue = Color.rgb(240,248,255);
        int gold =Color.rgb(255,215,0);
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        String[] labels = getXAxisValues(period);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setAxisMaximum(14 - 1.1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(7, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);



        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        float[] valOne;
        float[] valTwo;
        float[] valThree;
        float[] valfour;
if (period == 1){
     valOne = new float[]{3, 2, 5, 3,1, 2 , 2};
     valTwo = new float[]{4, 1, 1, 3, 2 , 8, 2};
     valThree = new float[]{4, 4, 4, 6, 1, 4,7};
     valfour = new float[]{2, 1, 9, 6, 3, 4, 2};
}else if (period == 2 ) {
     valOne = new float[]{30, 12, 5, 13, 11};
     valTwo = new float[]{14, 12, 21, 23, 20 };
     valThree = new float[]{34, 24, 40, 16,27};
     valfour = new float[]{23, 17, 19, 26, 30};
}else{
     valOne = new float[]{60, 22, 15, 43, 21,43, 47, 19, 66, 36,23, 10};
     valTwo = new float[]{44, 42, 51, 23, 10 ,36, 24, 40, 76,43, 21,22};
     valThree = new float[]{36, 24, 40, 76,15, 43, 21,43, 47, 19,22, 15};
     valfour = new float[]{43, 47, 19, 66, 36,36, 24,51, 23, 15, 43, 21};
}

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ArrayList<BarEntry> barThree = new ArrayList<>();
        ArrayList<BarEntry> barFour = new ArrayList<>();
        ArrayList<BarEntry> barFive = new ArrayList<>();
        ArrayList<BarEntry> barSix = new ArrayList<>();
        ArrayList<BarEntry> barSeven = new ArrayList<>();

        for (int i = 0; i < valfour.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            barThree.add(new BarEntry(i, valThree[i]));
            barFour.add(new BarEntry(i, valfour[i]));
            barFive.add(new BarEntry(i, valfour[i]));
            barSix.add(new BarEntry(i, valfour[i]));
            barSeven.add(new BarEntry(i, valfour[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "Sience");
        set1.setColor(darkBlue);
        BarDataSet set2 = new BarDataSet(barTwo, "Art");
        set2.setColor(aliceBlue);
        BarDataSet set3 = new BarDataSet(barThree, "Space");
        set3.setColor(gold);
        BarDataSet set4 = new BarDataSet(barFour, "Animal");
        set4.setColor(lightBlue);


        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);
        set3.setHighlightEnabled(false);
        set4.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);
        set4.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);

        BarData data = new BarData(dataSets);
        float groupSpace = 0.55f;
        float barSpace = 0.01f;
        float barWidth = 0.1f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left

      //  mChart.getXAxis().setValueFormatter();
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(6f);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.setBackgroundColor(Color.WHITE);
        mChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        mChart.animateX(1000);
       // mChart.showContextMenu();
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        mChart.getLegend().setEnabled(true);


    }

}
