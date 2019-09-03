package com.wce.barclays;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wce.barclays.adapters.ChatAdapter;
import com.wce.barclays.data.BuyOutput.Buy;
import com.wce.barclays.data.BuyOutput.BuyOp;
import com.wce.barclays.data.testrazor.TestRazor;
import com.wce.barclays.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements  TestRazor.TextRazorListener, Buy.BuyListener, Buy.SellListener {


    String numStocks;
    String price;
    String query;

    private int condition=0;
    //0-BUY 1-SELL 2-INPUTNUMSTOCKS 3-INPUTPRICE
    Toolbar toolbar;
    ChatAdapter chatAdapter;
    RecyclerView chats;
    EditText compose;
    ImageButton sendBtn;
    LinearLayout composeParent;
    FrameLayout frame;
    LinearLayoutManager mLinearLayoutManager;
    TestRazor testRazor;
    Buy buy;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLightStatusBar(this, Color.WHITE);
        testRazor = new TestRazor();
        buy = new Buy();
        frame = findViewById(R.id.frame);
        progressBar = findViewById(R.id.progress);
        composeParent = findViewById(R.id.compose_parent);
        chats = findViewById(R.id.chats);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sendBtn = findViewById(R.id.send_btn);
        compose = findViewById(R.id.compose_edit_text);
        chatAdapter = new ChatAdapter();
        mLinearLayoutManager  = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        chats.setLayoutManager(mLinearLayoutManager);
        chats.setAdapter(chatAdapter);
        compose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()) {
                    sendBtn.setEnabled(false);
                    sendBtn.setAlpha(0.7f);
                }

                else{
                    sendBtn.setAlpha(1f);
                    sendBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text  = compose.getText().toString();
compose.getText().clear();
                chatAdapter.addText(text, ChatAdapter.RIGHT);
             //   scroll();
                if(condition==2)
                {
                    if(isNumeric(text))
                    numStocks = text;
                    else {
                        chatAdapter.addText("Sorry, I couldn't recognize that", ChatAdapter.LEFT);
                    return;
                    }
                        if(price ==null)
                    {
                        condition = 3;
                        chatAdapter.addText("Enter purchased price of stocks",ChatAdapter.LEFT);
                    }
                    else
                    {
                        callSellApi();
                    }
                    return;
                }
                else if(condition==3)
                {
                    if(isNumeric(text))
                    price = text;
                    else
                    {
                        chatAdapter.addText("Sorry, I couldn't recognize that", ChatAdapter.LEFT);
                        return;
                    }
                    if(numStocks ==null)
                    {
                        condition = 2;
                        chatAdapter.addText("Enter number of stocks",ChatAdapter.LEFT);
                    }
                    else
                    {
                        callSellApi();
                    }
                    return;
                }
if(text.contains("buy")||text.contains("Buy"))
{
    TransitionManager.beginDelayedTransition(frame);
    progressBar.setVisibility(View.VISIBLE);
    composeParent.setVisibility(View.GONE);
    testRazor.getCompanyName(text,MainActivity.this);
}
else if(text.contains("sell"))
{
    query = text;

    String[] splits = text.split(" ");

    for(int i=0;i<splits.length;i++)
    {
        String split = splits[i];
        if(isNumeric(split) && i!=splits.length-1 && (splits[i + 1].equals("stocks") || splits[i + 1].equals("stock")))
        {
           // Toast.makeText(MainActivity.this, "IN STOCK", Toast.LENGTH_SHORT).show();
            numStocks = split;
        }
        else if(isNumeric(split))
        {
            price = split;
        }
    }
    if(numStocks==null)
    {
        condition=2;
        chatAdapter.addText("Enter number of stocks", ChatAdapter.LEFT);
        return;
    }
   else if(price==null)
    {
        condition=3;
        chatAdapter.addText("Enter purchased price of stocks", ChatAdapter.LEFT);
        return;
    }
    else {
        callSellApi();
    }
}
else
{
    chatAdapter.addText("Invalid Query",ChatAdapter.LEFT);
}
            }
        });

       // testRazor.getCompanyName("I would like to buy 100 stocks of Google",this);

    }

    private void callSellApi() {
        if(query!=null)
        {
            hideProgressBar(false);
            condition=1;
            testRazor.getCompanyName(query,this);
        }
    }



    @Override
    public void onSuccess(Word response) {



        List<String> text = response.getEntities().getOrganization();
        if(text==null) {
            hideProgressBar(true);
            chatAdapter.addText("Invalid company name", ChatAdapter.LEFT);
//            chatAdapter.addText("Amazon", ChatAdapter.LEFT);
//            chatAdapter.addText("Stock has moved from 1550 to 1780 and is expected to rise by 15% in future. So it is suggested to buy the stocks.", ChatAdapter.LEFT);
//            chatAdapter.addText("sell stocks of google", ChatAdapter.RIGHT);
//            chatAdapter.addText("Google", ChatAdapter.LEFT);
//            chatAdapter.addText("Stock has moved from 1280 to 1200 and is expected to fall by 20% in future. So it is suggested to wait.", ChatAdapter.LEFT);

            numStocks=price=null;
        return;
        }
        if(condition==1)
        {
            buy.getSellResult(text.get(0),numStocks,price,this);
            condition =0;
            return;
        }
        buy.getResult(response.getEntities().getOrganization().get(0),this);
        chatAdapter.addText(response.getEntities());

    }

    @Override
    public void onSuccess(BuyOp response) {
        chatAdapter.addText("Recommendation : " + response.getStatus(),ChatAdapter.LEFT);

        chatAdapter.addText("Explanation : "+response.getReason(), ChatAdapter.LEFT);

        hideProgressBar(true);

    }

    private void hideProgressBar(boolean hide)
    {
        if(progressBar.getVisibility()==View.GONE && hide)
            return;
        TransitionManager.beginDelayedTransition(frame);
        progressBar.setVisibility(hide?View.GONE:View.VISIBLE);
        composeParent.setVisibility(hide?View.VISIBLE:View.GONE);
    }

    @Override
    public void onFailure(String t) {
        Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
        hideProgressBar(true);

    }

    private void scroll()
    {
        int friendlyMessageCount = chatAdapter.getChats().size();
        int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (lastVisiblePosition == -1 ||
                (friendlyMessageCount-1 >= (friendlyMessageCount - 1) && lastVisiblePosition == (friendlyMessageCount-3))) {
            chats.scrollToPosition(friendlyMessageCount-1);
        }
    }


    public void getSpeechInput(View view)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    compose.setText(result.get(0));
                   sendBtn.callOnClick();

                }
                break;
        }
    }


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public void onSellSuccess(BuyOp response) {
        chatAdapter.addText("Recommendation : " + response.getStatus(),ChatAdapter.LEFT);
hideProgressBar(true);
        chatAdapter.addText("Explanation : "+response.getReason(), ChatAdapter.LEFT);
        numStocks=price = null;
    }

    @Override
    public void onSellFailure(String t) {
        Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
        hideProgressBar(true);
    }
    public static void setLightStatusBar(Activity context, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            context.getWindow().setStatusBarColor(color);
            final View view = context.getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }
}
