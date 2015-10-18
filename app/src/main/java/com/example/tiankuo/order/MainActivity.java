package com.example.tiankuo.order;

import android.app.TabActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;

import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent=new Intent().setClass(this, SearchActivity.class);
        spec=tabHost.newTabSpec("订房").setIndicator("订房").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, ProfileActivity.class);
        spec=tabHost.newTabSpec("我").setIndicator("我").setContent(intent);
        tabHost.addTab(spec);

        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_tab_search://添加考试
                        tabHost.setCurrentTabByTag("订房");
                        break;
                    case R.id.main_tab_profile://我的考试
                        tabHost.setCurrentTabByTag("我");
                        break;
                    default:
                        tabHost.setCurrentTabByTag("订房");
                        break;
                }
            }
        });
    }

}
