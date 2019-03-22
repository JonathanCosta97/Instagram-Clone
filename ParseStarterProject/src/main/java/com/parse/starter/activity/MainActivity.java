/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;
import com.parse.starter.tabsAdapter.TabsAdapter;
import com.parse.starter.util.SlidingTabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.parse.starter.fragments.homeFragment;


public class MainActivity extends AppCompatActivity {

  private Toolbar toolbarPrincipal;
  private SlidingTabLayout slidingTabLayout;
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Configura toolbar
    toolbarPrincipal = (Toolbar) findViewById(R.id.toolbarPrincipal);
    toolbarPrincipal.setLogo( R.drawable.instagramlogo );
    setSupportActionBar( toolbarPrincipal );

    //Configura abas
    slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stLayout);
    viewPager = (ViewPager) findViewById(R.id.viewpagerID);

    //configurar adapter
    TabsAdapter tabsAdapter = new TabsAdapter( getSupportFragmentManager(), this );
    viewPager.setAdapter( tabsAdapter );
    slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.text_item);
    slidingTabLayout.setDistributeEvenly(true);
    slidingTabLayout.setSelectedIndicatorColors( ContextCompat.getColor(this, R.color.cinzaEscuro) );
    slidingTabLayout.setViewPager( viewPager );
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()){

      case R.id.action_Configuracao:
        return true;

      case R.id.action_Sair:
        deslogarUsuario();
        return true;

      case R.id.action_Compartilhar:
        compartilharFoto();
        return true;

      default: return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Testar processo de retorno dos dados
    if (requestCode==1 && resultCode == RESULT_OK && data !=null){

      //Recuperar local do recurso
      Uri localImagemSelecionada = data.getData();

      //Recuperar imagem do local que foi selecionada
      try {
        Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

        //Comprimir no formato PNG
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.PNG, 75, stream);

        //Criar um array de byte da imagem
        byte[] byteArray = stream.toByteArray();

        //Criar um arquivo com um formato proprio do Parse
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddmmaaaahhmmss");
        String nomeImagem = dateFormat.format(new Date());
        ParseFile arquivoParse = new ParseFile(nomeImagem +"imagem.PNG", byteArray);

        //Montar o objeto para salvar no Parse
        ParseObject object = new ParseObject("Imagem");
        object.put("username", ParseUser.getCurrentUser().getUsername());
        object.put("imagem", arquivoParse);

        //Salvar dados
        object.saveInBackground(new SaveCallback() {
          @Override
          public void done(ParseException e) {

            if (e == null){
              Toast.makeText(MainActivity.this,"Sua imagem foi postada!", Toast.LENGTH_LONG).show();

              //Atualizar a lista de itens do Fragmento HOME
                TabsAdapter adp = (TabsAdapter) viewPager.getAdapter();
                homeFragment hf = (homeFragment) adp.getFragment(0);
                hf.atualizaPostagens();

            }else {
              Toast.makeText(MainActivity.this,"Erro ao postar a sua imagem - Tente novamente!", Toast.LENGTH_LONG).show();
            }
          }
        });

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void compartilharFoto(){
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent,1);

  }

  private void deslogarUsuario(){
    ParseUser.logOut();
    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
  }
}


