package com.parse.starter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText senha;
    private Button logar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        usuario = (EditText) findViewById(R.id.edtUsuario);
        senha = (EditText) findViewById(R.id.edtSenha);
        logar = (Button) findViewById(R.id.btnLogar);

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = usuario.getText().toString();
                String senhaUsuario = senha.getText().toString();

                autenticarUsuario(nomeUsuario, senhaUsuario);
            }
        });
    }

    public void autenticarUsuario(String usuario, String senha){
        ParseUser.logInInBackground(usuario, senha, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null){
                    Toast.makeText(LoginActivity.this,"Login efetuado com Sucesso!", Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();
                }else{
                    Toast.makeText(LoginActivity.this,"Erro ao efetuar Login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verificarUsuarioLogado(){

        if (ParseUser.getCurrentUser() != null){
           abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirTelaCadastro(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
        finish();
    }
}
