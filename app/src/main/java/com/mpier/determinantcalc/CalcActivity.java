package com.mpier.determinantcalc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class CalcActivity extends AppCompatActivity {

    TextView mTextView, mResult, mTime;
    LinearLayout mLinearLayout;
    TableLayout mTableLayout;
    int mDimension;
    double mArray[][], mResultValue;
    Random rn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        mTextView = (TextView) findViewById(R.id.item);
        Intent intent = getIntent();

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            mTextView.setText("no data");
        } else {
            mDimension = intent.getIntExtra("number", 0);
            mTextView.setText("Calculating: "+String.valueOf(mDimension)+" x "+String.valueOf(mDimension));
        }

        mArray = new double[mDimension][mDimension];

        mLinearLayout = (LinearLayout) findViewById(R.id.table_matrix);
        mTableLayout = new TableLayout(getApplicationContext());

        mLinearLayout.addView(getTableWithAllRowsStretchedView());


        mResult = (TextView) findViewById(R.id.result);
        mTime = (TextView) findViewById(R.id.time);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    public View getTableWithAllRowsStretchedView() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        tableLayout.setWeightSum(mDimension);


        for (int i = 0; i < mDimension; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

            for (int j = 0; j < mDimension; j++) {
                TextView tv = new TextView(this);
                tv.setGravity(Gravity.CENTER);
                rn = new Random();
                mArray[i][j]=rn.nextInt(9) + 1;
                tv.setText(String.valueOf((int)mArray[i][j]));
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tv.setBackgroundColor(setBackColor(i+j));

                tableRow.addView(tv);
            }
            tableLayout.addView(tableRow);
        }

        linearLayout.addView(tableLayout);
        return linearLayout;
    }

    int setBackColor(int value) {
        if(value%2==0)
            return Color.WHITE;
        else
            return Color.LTGRAY;
    }

    public double determinant(double A[][],int N)
    {
        double det=0;
        if(N == 1)
        {
            det = A[0][0];
        }
        else if (N == 2)
        {
            det = A[0][0]*A[1][1] - A[1][0]*A[0][1];
        }
        else
        {
            det=0;
            for(int j1=0;j1<N;j1++)
            {
                double[][] m = new double[N-1][];
                for(int k=0;k<(N-1);k++)
                {
                    m[k] = new double[N-1];
                }
                for(int i=1;i<N;i++)
                {
                    int j2=0;
                    for(int j=0;j<N;j++)
                    {
                        if(j == j1)
                            continue;
                        m[i-1][j2] = A[i][j];
                        j2++;
                    }
                }
                det += Math.pow(-1.0,1.0+j1+1.0)* A[0][j1] * determinant(m,N-1);
            }
        }
        return det;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;
        long startTime, endTime;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                mResultValue = determinant(mArray, mDimension);
                resp = "Calculated for " + params[0] + " seconds";
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            endTime = System.currentTimeMillis();
            progressDialog.dismiss();
            mResult.setText("Result: "+String.valueOf((long)mResultValue));
            mTime.setText(calcTime(endTime, startTime));
        }


        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
            progressDialog = ProgressDialog.show(CalcActivity.this,
                    "Calculation",
                    "Calculating the determinant of "+mDimension+" x "+mDimension+" matrix may take some time...");
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    String calcTime(long endTime, long startTime) {
        long mili = (endTime-startTime)%1000;
        long sec = (endTime-startTime)/1000;
        long min = 0;
        if(sec>=60)
        {
            min = sec/60;
            sec = sec%60;
        }

        return "Calculated in: "+String.valueOf(min)+" min  "+String.valueOf(sec)+" sec  "+String.valueOf(mili)+"mili";
    }
}
