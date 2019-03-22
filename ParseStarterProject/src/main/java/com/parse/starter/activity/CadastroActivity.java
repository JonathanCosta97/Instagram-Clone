package com.parse.starter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;
import com.parse.starter.util.ParseErros;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button cadastrar;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = (EditText) findViewById(R.id.edtNome);
        email = (EditText) findViewById(R. id.edtUsuario);
        senha = (EditText) findViewById(R.id.edtSenha);
        cadastrar = (Button) findViewById(R.id.btnLogar);
        txtLogin = (TextView) findViewById(R.id.txtIrLogin);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarUsuario();
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLogin();
            }
        });
    }

    public void CadastrarUsuario(){
        ParseUser usuario = new ParseUser();
        usuario.setUsername(nome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setPassword(senha.getText().toString());

        usuario.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null){
                    Toast.makeText(CadastroActivity.this,"Usuario cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    abrirLogin();
                }else {
                    ParseErros parseErros = new ParseErros();
                    String erro = parseErros.getErro(e.getCode());
                    Toast.makeText(CadastroActivity.this,erro,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirLogin(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
