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
        //TODO:获取酒店列表，修改hotel_list
        hotel_list.add("没有合适的酒店显示");
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try{
            Socket socket = new Socket(host, port);
        }catch (UnknownHostException e){
            ;
        }catch(IOException e){
            ;
        }

        get_hotel_list();
        lv = (ListView) findViewById(R.id.hotel_list);
        ad = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, hotel_list);
        lv.setAdapter(ad);
    }

    public  void startSearch(View view){
        //根据输入内容向服务器发请求搜索
        //构建查询语句
        String search_message = "INPUT:1005,INDEX:0,NUM:10,KEYWORD:";
        String keyword = ((EditText) findViewById(R.id.search_message)).getText().toString();
        search_message+= keyword;

        hotel_list.clear();
        hotel_list.add("酒店名称 价格 地址");
        hotel_list.add("酒店名称 价格 地址");
        hotel_list.add("酒店名称 价格 地址");
        ad.notifyDataSetChanged();
    }
}
