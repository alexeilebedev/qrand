package com.alexeilebedev.qrand;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

// Query public quantum random number generator
public class Qrand {
    public final String _url="https://qrng.anu.edu.au/API/jsonI.php";
    IntBuffer _data;
    static final String _datatype="uint8";
    String _log;
    String _error;

    Qrand() {
        _data = IntBuffer.allocate(10);
        _data.limit(0);
        _data.position(0);
    }

    private String getUrl(String str) {
        String ret="";
        try {
            URL url = new URL(str);
            InputStream is = url.openStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            ret = s.hasNext() ? s.next() : "";
        } catch (MalformedURLException e) {
            _error=e.toString();
        } catch (IOException e) {
            _error=e.toString();
        }
        return ret;
    }

    // discard remaining contents of _Data.
    // query more data from the random server, and append
    // it to the buffer
    // prepare buffer for reading
    private void refill() {
        _data.position(0);
        try {
            int wantlen = _data.capacity();
            String json = getUrl(_url + String.format("?length=%d&type=%s", wantlen,_datatype));
            Log.w("json", json);
            JSONTokener tokener = new JSONTokener(json);
            JSONObject object = (JSONObject) tokener.nextValue();
            String type = object.getString("type");
            int length = object.getInt("length");
            boolean success = object.getBoolean("success");
            JSONArray values = object.getJSONArray("data");
            // expect reply such as
            // {"type":"uint8","length":10,"data":[128,39,160,40,195,37,43,70,82,250],"success":true}
            // put ...
            if (success == true && type.equals(_datatype) && length == wantlen && values != null) {
                _data.limit(wantlen);
                for (int i = 0; i < wantlen; i++) {
                    _data.put(values.getInt(i));
                }
            }
            _data.position(0);
        } catch (JSONException e) {
            _error=e.toString();
        }
    }

    // retrieve next u8 from buffer
    // even though we use an int buffer, we retrieve uint8s from the server.
    // refill buffer if necessary
    // return -1 if invalid
    int readu8() {
        _log =null;
        _error=null;
        int ret=-1;
        if (_data.remaining()==0) {
            refill();
        }
        if (_data.remaining()>0) {
            ret = _data.get();
        }
        return ret;
    }
}
