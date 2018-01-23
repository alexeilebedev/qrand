package com.alexeilebedev.qrand;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;

public class Textview extends EditText {
    Textview(Context ctx) {
        super(ctx);
        setGravity(Gravity.TOP);
        setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setSingleLine(false);
    }
}
