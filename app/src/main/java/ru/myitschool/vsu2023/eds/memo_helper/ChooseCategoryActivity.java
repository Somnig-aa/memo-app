package ru.myitschool.vsu2023.eds.memo_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryActivity extends AppCompatActivity {
    private Button refreshBtn;
    private ListView listOfCat;
    private CategoryAdapter adapter;
    private class CategoryAdapter extends BaseAdapter{
        private List<Category> categories=new ArrayList<>();
        private LayoutInflater mLayoutInflater;
        public CategoryAdapter(Context ctx){
            mLayoutInflater=LayoutInflater.from(ctx);
        }
        public void refresh(List<Category> cat){
            categories.clear();
            categories.addAll(cat);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return categories.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.category_list_item, null);

            TextView categ = (TextView)convertView.findViewById(R.id.name_category);


            Category md = categories.get(position);
            categ.setText(md.getName());


            return convertView;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        adapter=new CategoryAdapter(this);
        listOfCat=findViewById(R.id.choose_category);
        listOfCat.setAdapter(adapter);
        loadFromDB();
        listOfCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(ChooseCategoryActivity.this, MainActivity.class);
                i.putExtra(MainActivity.CATEGORY_NUMBER_ARG,id);
                startActivity(i);
            }
        });
    }
    private void showCat(List<Category> categoryList){
        adapter.refresh(categoryList);

    }
    private void loadFromDB(){
        new CategoriesLoaderFromDB().execute() ;
    }
    class CategoriesLoaderFromDB extends AsyncTask<Void,Void, List<Category>>{

        @Override
        protected List<Category> doInBackground(Void... voids) {
            DBmemo db =new DBmemo(ChooseCategoryActivity.this);
            return db.selectAllCat();
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            showCat(categories);
        }
    }
}