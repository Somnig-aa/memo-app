package ru.myitschool.vsu2023.eds.memo_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkActivity extends AppCompatActivity {
    private Button letterBtn1;
    private Button letterBtn2;
    private Button homeBtn;
    private TextView quesText;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        letterBtn1=findViewById(R.id.letter_btn1);
        letterBtn2=findViewById(R.id.letter_btn2);
        homeBtn=findViewById(R.id.home_btn);
        quesText=findViewById(R.id.question_text_tv);
        image=findViewById(R.id.hint_image_iv);

    }
    private void showQuestion(Question quest){
        quesText.setText(quest.getText());
        letterBtn1.setText(quest.getRightLet());
        letterBtn2.setText(quest.getWrongLet());
        if(!quest.getImageName().isEmpty()){

        }

    }

}