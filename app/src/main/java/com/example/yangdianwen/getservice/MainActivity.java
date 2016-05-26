package com.example.yangdianwen.getservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    String JsonStr;
    private HttpURLConnection mHttpURLConnection = null;
    private Handler mMHandler;
    final int flag = 1;
    private JSONObject mJs;
    private List<Student> mList;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);


    }

    public void quary(View view) {
        //创建线程执行数据获取方法

        new Thread() {
            public void run() {
                try {
                    get();
                    //向Handler发送消息
                    mMHandler.sendEmptyMessage(flag);
                    //调用解析方法

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        //使用Handler发送消息
        mMHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case flag:
                        parseEasyJson(JsonStr);
                        break;
                }
            }
        };
    }


    private void get() throws IOException {
        BufferedReader reader;

        HttpURLConnection urlConnection;
        try {
            //获取网络路径
         URL url = new URL("http://192.168.1.147:8080/index2.jsp");

        //打开链接l
            urlConnection = (HttpURLConnection) url.openConnection();
            //请求方式
            urlConnection.setRequestMethod("GET");
            //连接
            urlConnection.connect();
            //获取网络流
            InputStream inputStream = urlConnection.getInputStream();
            //定义缓冲区
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            //读取数据，读到的每一行不为null时，把数据添加到缓冲区
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return;
            }
            //把缓冲区数据转成字符串 json语句
            JsonStr = buffer.toString();
            Log.d(TAG, "get: " + JsonStr);
        } catch (Exception e) {

        }
    }
    public void parseEasyJson(String json) {
        mList = new ArrayList<>();
        try {
            //解析json语句获取JSONObject对象
            JSONObject jsonObject=new JSONObject(json);
            //解析数组
            JSONArray jsonArray=jsonObject.getJSONArray("students");
            //for循环遍历数组得到其中数组中的每个对象的具体信息
            for (int i = 0; i <jsonArray.length() ; i++) {
                mJs = (JSONObject) jsonArray.get(i);
                Student student=new Student();
                 String name = (String) mJs.get("name");
                int age = (int) mJs.get("age");
                student.setName(name);
                student.setAge(age);
                mList.add(student);
                Log.d(TAG, "parseEasyJson: "+name+age);
              Log.d(TAG, "parseEasyJson: "+mJs);
            }
            mTextView.setText(mList.get(0).getName()+mList.get(0).getAge());
          Log.d(TAG, "parseEasyJson: "+jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
