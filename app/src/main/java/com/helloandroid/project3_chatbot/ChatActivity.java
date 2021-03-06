package com.helloandroid.project3_chatbot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    EditText userInput;
    ImageView button_send;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdeapter messageAdeapter;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    responseAction();
                }
                return true;
            }
        });
    }

    private void init() {
        userInput = (EditText) findViewById(R.id.userInput);
        button_send = (ImageView) findViewById(R.id.button_send);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdeapter = new MessageAdeapter(responseMessageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(messageAdeapter);

        buttonAction();
    }

    private void buttonAction() {
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseAction();
            }
        });
    }

    private void responseAction() {
        String currentData = userInput.getText().toString();
        String resultData = null;

        if ("안녕".equals(currentData) || "하이".equals(currentData) || "반가워".equals(currentData) || "헬로".equals(currentData)) {
            resultData = SelectWelcome();
        } else if ("끝끝끝".equals(currentData)) {
            selectPhoto();
            resultData = "일기 저장 완료. 오늘 하루도 수고 많았어. 내일 또 봐. 안녕!";
        } else {
            resultData = "일기를 저장할까? 저장하려면 '끝끝끝'이라고 보내고 사진을 선택해줘.";
        }
        ResponseMessage message = new ResponseMessage(currentData, true);
        responseMessageList.add(message);
        ResponseMessage message2 = new ResponseMessage(resultData, false);
        responseMessageList.add(message2);
        messageAdeapter.notifyDataSetChanged();

        userInput.setText("");

        if(!isVisivle()){
            recyclerView.smoothScrollToPosition(messageAdeapter.getItemCount()-1);
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String SelectWelcome() {
        String[] Welcomelist = {"반가워! 오늘 너의 하루는 어땠는지 들려줘.", "안녕, 오늘 하루 어땠어?",
                "보고싶었어! 오늘 하루 어떻게 보냈어?", "헬로! 오늘은 어떤 일이 있었어?", "하이! 오늘은 어땠어, 재미있는 하루였어?"};

        Random random = new Random();
        String selectWelcome = Welcomelist[random.nextInt(Welcomelist.length)];
        return selectWelcome;
    }

    public boolean isVisivle(){
        LinearLayoutManager LinearlayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int positionOfLastVisivleItem = LinearlayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = recyclerView.getAdapter().getItemCount();
        return  (positionOfLastVisivleItem>=itemCount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    Drawable drawable = new BitmapDrawable(bitmap);
                    //ArrayList<CalendarDay> dates = new ArrayList<>();
                    //dates.add(new CalendarDay(Year,Month,Day));

                    //materialCalendarView.addDecorator(new EventDecorator(drawable, dates,MainActivity.this));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
