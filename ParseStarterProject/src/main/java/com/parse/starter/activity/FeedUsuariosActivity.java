package com.parse.starter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.R;
import com.parse.starter.tabsAdapter.HomeAdpter;

import java.util.ArrayList;
import java.util.List;

public class FeedUsuariosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView feedUsuario;
    private String userName;
    private ArrayAdapter<ParseObject> adapter;
    private ArrayList<ParseObject> postagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_usuarios);

        //Recuperar dados passados pela Intent
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");

        toolbar = (Toolbar) findViewById(R.id.toolbar_feed_usuario);
        toolbar.setTitle(userName);
        toolbar.setTitleTextColor(R.color.preto);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left);
        setSupportActionBar(toolbar);

        postagens = new ArrayList<>();
        feedUsuario = (ListView) findViewById(R.id.listFeedUsuario);
        adapter = new HomeAdpter(getApplicationContext(), postagens);
        feedUsuario.setAdapter(adapter);

        getPostagens();

    }

    private void getPostagens(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Imagem");
        query.whereEqualTo("username", userName);
        query.orderByAscending("createAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e==null){

                    if (objects.size() >0){
                        postagens.clear();
                        for (ParseObject parseObject : objects){
                            postagens.add(parseObject);
                        }
                        adapter.notifyDataSetChanged();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Erro ao recuperar feed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
