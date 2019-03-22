package com.parse.starter.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.activity.FeedUsuariosActivity;
import com.parse.starter.tabsAdapter.UsuariosAdapter;

import java.util.ArrayList;
import java.util.List;

public class UsuarioFragment extends Fragment {

    private ListView listUsuarios;
    private ArrayAdapter<ParseUser> adapter;
    private ArrayList<ParseUser> usuarios;
    private ParseQuery<ParseUser> query;

    public UsuarioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);

        usuarios = new ArrayList<>();
        listUsuarios = (ListView) view.findViewById(R.id.lstUsuarios);
        adapter = new UsuariosAdapter(getActivity(),usuarios);
        listUsuarios.setAdapter(adapter);
        getUsuarios();

        //Evento de click
        listUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Retornar dados do usario a ser passados.
                ParseUser parseUser = usuarios.get(position);

                //Enviar dados para o feed
                Intent intent = new Intent(getActivity(), FeedUsuariosActivity.class);
                intent.putExtra("username", parseUser.getUsername());
                startActivity(intent);
            }
        });

        return view;
    }

    private void getUsuarios(){

        query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null){

                    if (objects.size() > 0){
                        usuarios.clear();

                        for (ParseUser parseUser : objects){
                            usuarios.add(parseUser);
                        }

                        adapter.notifyDataSetChanged();
                    }

                }else {
                    e.printStackTrace();
                }
            }
        });
    }

}
