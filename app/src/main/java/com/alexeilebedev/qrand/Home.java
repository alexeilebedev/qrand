package com.alexeilebedev.qrand;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;

import java.util.Date;

public class Home extends AppCompatActivity {
    Textview _textview;
    Qrand _qrand;

    Home() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _qrand = new Qrand();
        super.onCreate(savedInstanceState);

        LinearLayout vlayout=new LinearLayout(this);
        vlayout.setOrientation(LinearLayout.VERTICAL);
        vlayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        _textview = new Textview(this);
        _textview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT, 1.f));
        _textview.append("Hello\n");
        _textview.append("Using " + _qrand._url + "\n");

        LinearLayout hlayout=new LinearLayout(this);
        hlayout.setOrientation(LinearLayout.HORIZONTAL);
        hlayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        Button btn2=addButton(hlayout,"mod 2",  2, null);
        Button btn3=addButton(hlayout,"mod 3",  3, btn2);
        Button btn4=addButton(hlayout,"mod 256",  256, btn3);

        vlayout.addView(_textview);
        vlayout.addView(hlayout);
        setContentView(vlayout);
    }

    static String stringOr(String a, String b) {
        return a!=null ? a : b;
    }
    void queryRand(int n) {
        Date date=new Date();
        int value = _qrand.readu8();
        _textview.append(stringOr(_qrand._log,""));
        _textview.append(String.format("date:%s  n:%s\n",date.toString(),n));
        _textview.scrollTo(0, _textview.getScrollY());
        if (value==-1) {
            _textview.append(stringOr(_qrand._error + "\n","error\n"));
        } else {
            _textview.append(String.format(">> %d\n", value % n));
        }
    }

    Button addButton(LinearLayout layout, String text,  final int n, Button prev) {
        // Creating a new Bottom Button
        Button btn = new Button(this);
        btn.setText(text);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        layout.addView(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                queryRand(n);
            }
        });
        return btn;
    }

}
