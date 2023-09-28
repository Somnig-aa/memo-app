package ru.myitschool.vsu2023.eds.memo_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class WorkActivity extends AppCompatActivity {
    private Button letterBtn1;
    private Button letterBtn2;
    private Button homeBtn;

    private TextView scoreTv;
    private TextView quesText;
    private ImageView image;
    private ImageView live1;
    private ImageView live2;
    private ImageView live3;
    private boolean isTreining = true;
    private long categoryId;

    private int livesCount = 0;

    private boolean isLeft=true;
    public static final String IS_TRAINING_MODE_ARG = "training mode";
    public static final String NUMBER_OF_CATEGORY_ARG = "number";

    public Question[] arr = new Question[]{

    };

    private int score = 0;
    private int current = 0;
    class QestionsLoaderFromDB extends AsyncTask<Void, Void, List<Question>>{

        @Override
        protected void onPostExecute(List<Question> questions) {
            arr = new Question[questions.size()];
            for (int i=0; i<questions.size(); i++){
                arr[i]=questions.get(i);
            }
            current=0;
            showCurrentQuestion();

        }

        @Override
        protected List<Question> doInBackground(Void... voids) {
            DBmemo db = new DBmemo(WorkActivity.this);
            return db.selectQuestOfCat(categoryId);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        letterBtn1 = findViewById(R.id.letter_btn1);
        letterBtn2 = findViewById(R.id.letter_btn2);
        homeBtn = findViewById(R.id.home_btn);
        quesText = findViewById(R.id.question_text_tv);
        image = findViewById(R.id.hint_image_iv);
        live1 = findViewById(R.id.live1);
        live2 = findViewById(R.id.live2);
        live3 = findViewById(R.id.live3);
        scoreTv=findViewById(R.id.score_tv);
        categoryId= getIntent().getLongExtra(WorkActivity.NUMBER_OF_CATEGORY_ARG,0);
        isTreining = getIntent().getBooleanExtra(WorkActivity.IS_TRAINING_MODE_ARG, true);
        score=0;
        if (isTreining) {
            image.setVisibility(View.VISIBLE);
     //       showImage(arr[current].getImageName());
        } else {
            livesCount = 3;
        }
        showCurrentProgress();
        //showQuestion(arr[current]);

        letterBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScore(isLeft);
                current++;
                gameOverCheck();
                if (current >= arr.length) {
                    current=0;
                }
                showCurrentQuestion();
            }
        });
        letterBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScore(!(isLeft));
                current++;
                gameOverCheck();
                if (current >= arr.length) {
                    current=0;
                }
                showCurrentQuestion();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*Intent i = new Intent(WorkActivity.this, MainActivity.class);
                i.putExtra(MainActivity.IS_HOME_MODE_ARG, true);
                startActivity(i);*/
            }
        });
        new QestionsLoaderFromDB().execute();
    }
    private void changeScore(boolean isCorrectAnswer){
        if (isCorrectAnswer){
            score+=1;
        } else {
            if (!(isTreining)){
                livesCount-=1;
            }
        }

    }
    private void showImage(String nameImage) {
        int rid = getResources().getIdentifier(nameImage, "raw", getPackageName());
        if (rid == 0) {
            return;
        }
        InputStream in = getResources().openRawResource(rid);
        Drawable hint = Drawable.createFromStream(in, nameImage);
        image.setImageDrawable(hint);
    }
    private void showCurrentProgress(){
        scoreTv.setText("Счёт: " + score );
        showLives(livesCount);
    }

    private void gameOverCheck(){
        if (!(isTreining)){
            if ((livesCount<=0)||(current==arr.length) ){

                String message="";

                if (livesCount<=0){
                    message="К сожалению, у вас закончились жизни :'( Ваш счёт: "+score;
                } else {
                    message="Ура! Вы победили :) Ваш счёт: "+score;
                }
                new AlertDialog.Builder(this)
                        .setTitle("ИГРА ОКОНЧЕНА")
                        .setMessage(message)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();

                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        }).create().show();
            }
        }
    }

    private void showLives(int col) {
        if (col > 0) {
            live1.setVisibility(View.VISIBLE);
        } else {
            live1.setVisibility(View.INVISIBLE);
        }
        if (col > 1) {
            live2.setVisibility(View.VISIBLE);
        } else {
            live2.setVisibility(View.INVISIBLE);
        }
        if (col > 2) {
            live3.setVisibility(View.VISIBLE);
        } else {
            live3.setVisibility(View.INVISIBLE);
        }
    }
    private void showCurrentQuestion(){
        showQuestion(arr[current]);
    }
    private void showQuestion(Question quest) {
        quesText.setText(quest.getText());
        isLeft=Math.random()<0.50;
        if (isLeft) {
            letterBtn1.setText(quest.getRightLet());
            letterBtn2.setText(quest.getWrongLet());
        } else {
            letterBtn2.setText(quest.getRightLet());
            letterBtn1.setText(quest.getWrongLet());
        }
        if (!quest.getImageName().isEmpty()) {
            showImage(quest.getImageName());
        }
        showCurrentProgress();
    }

}