package com.itchunyang.popmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

//PopupMenu显示效果类似上下文菜单(Menu)，而PopupWindow的显示效果实际上类似对话框(Dialog)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //它会自适应位置，在按钮的左下角或者左上角显示
    public void popMenu(View view) {
        PopupMenu menu = new PopupMenu(this,view);
        menu.inflate(R.menu.pop_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        menu.show();
    }
}
