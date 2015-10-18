package com.example.tiankuo.order;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.*;

public class SearchActivity extends AppCompatActivity {
    int port = 9981;
    String host = "127.0.0.1";
    Socket socket;

    private static ArrayList<String> hotel_list = new ArrayList<String>();
    private ListView lv;
    private ArrayAdapter ad;

    private void get_hotel_list(){
        //获取酒店列表，修改hotel_list
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("INPUT","1004");
        map.put("INDEX","0");
        //TODO:将获取的数量改为动态的
        map.put("NUM","10");
        //将json转化为String类型
        JSONObject json = new JSONObject(map);
        String jsonString = json.toString();
        //将String转化为byte[]
        //byte[] jsonByte = new byte[jsonString.length()+1];
        byte[] jsonByte = jsonString.getBytes();
        DataOutputStream outputStream = null;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.write(jsonByte);
            outputStream.flush();
        }catch (IOException e) {
            ;
        }

        try{
            DataInputStream inputStream =  new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String strInputstream = inputStream.readUTF();
            JSONObject js = new JSONObject(strInputstream);
            if(js.get("RESULT") == "30041"){
                hotel_list.clear();
                JSONArray hotel_array = js.getJSONArray("hotel");
                for(int i = 0; i<hotel_array.length(); i++){
                    JSONObject json_hotel = hotel_array.getJSONObject(i);
                    String hotel_name = (String) json_hotel.get("HOTELNAME");
                    String price = (String) json_hotel.get("PRICE");
                    String address = (String) json_hotel.get("ADDRESS");
                    hotel_list.add(hotel_name +" " +price +" "+address);
                }
            }else if(js.get("RESULT") == "30042"){
                hotel_list.clear();
                hotel_list.add("获取酒店列表失败");
            }
        }catch(Exception e){

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try{
            Socket socket = new Socket(host, port);
            get_hotel_list();
        }catch (UnknownHostException e){
            System.out.println("无法连接到指定host:" + e.getMessage());
        }catch(IOException e){
            ;
        }

        lv = (ListView) findViewById(R.id.hotel_list);
        ad = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, hotel_list);
        lv.setAdapter(ad);
    }

    public  void startSearch(View view){
        try {
            //根据输入内容向服务器发请求搜索
            //构建查询语句
            String keyword = ((EditText) findViewById(R.id.search_message)).getText().toString();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("INPUT", "1005");
            map.put("INDEX", "0");
            //TODO:将获取的数量改为动态的
            map.put("NUM", "10");
            map.put("KEYWORD", keyword);
            JSONObject json = new JSONObject(map);
            String jsonString = json.toString();
            byte[] jsonByte = jsonString.getBytes();
            DataOutputStream outputStream = null;
            try {
                outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.write(jsonByte);
                outputStream.flush();
            } catch (Exception e) {
                System.out.print("无法获得socket的输出流：" + e.getMessage());
            }
            //接收查询结果
            try {
                DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                String strInputstream = inputStream.readUTF();
                JSONObject js = new JSONObject(strInputstream);
                if (js.get("RESULT") == "30051") {
                    hotel_list.clear();
                    JSONArray hotel_array = js.getJSONArray("hotel");
                    for (int i = 0; i < hotel_array.length(); i++) {
                        JSONObject json_hotel = hotel_array.getJSONObject(i);
                        String hotel_name = (String) json_hotel.get("HOTELNAME");
                        String price = (String) json_hotel.get("PRICE");
                        String address = (String) json_hotel.get("ADDRESS");
                        hotel_list.add(hotel_name + " " + price + " " + address);
                    }
                } else if (js.get("RESULT") == "30042") {
                    hotel_list.clear();
                    hotel_list.add("没有合适的酒店");
                }
            } catch (Exception e) {
                hotel_list.clear();
                hotel_list.add("无法进行查询");
                System.out.print("无法接收查询结果:" +e.getMessage());
            }

            ad.notifyDataSetChanged();
        }catch (Exception e){
            ;
        }
    }
}
