package ru.myitschool.vsu2023.eds.memo_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button trainBtn;
    private Button testBtn;

    public final static String CATEGORY_NUMBER_ARG = "numOfCat";
    private long categoryId;

    private TextView categoryNameTV;
    class CategoryLoaderFromDB extends AsyncTask<Void,Void,Category>{

        @Override
        protected Category doInBackground(Void... voids) {
            DBmemo db= new DBmemo(MainActivity.this);
            return db.selectCat(categoryId);
        }

        @Override
        protected void onPostExecute(Category category) {
            categoryNameTV.setText(category.getName());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryId=getIntent().getLongExtra(CATEGORY_NUMBER_ARG, 0);
        trainBtn=findViewById(R.id.start_training_btn);
        testBtn=findViewById(R.id.start_test_btn);
        categoryNameTV=findViewById(R.id.category_name);
        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, WorkActivity.class);
                i.putExtra(WorkActivity.IS_TRAINING_MODE_ARG,true);
                i.putExtra(WorkActivity.NUMBER_OF_CATEGORY_ARG,categoryId);
                startActivity(i);
            }
        });
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,WorkActivity.class);

                i.putExtra(WorkActivity.IS_TRAINING_MODE_ARG,false);
                i.putExtra(WorkActivity.NUMBER_OF_CATEGORY_ARG, categoryId);
                startActivity(i);
            }
        });
        new CategoryLoaderFromDB().execute();
    }
}