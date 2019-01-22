package com.example.ricindigus.booklistingapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListaAdapter extends ArrayAdapter<Libro> {

    public ListaAdapter(Context context,List<Libro> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Libro currentItem = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.libro_item,parent,false);
        }

        TextView title = convertView.findViewById(R.id.txtTitulo);
        TextView autores = convertView.findViewById(R.id.txtAutores);
        TextView editorial = convertView.findViewById(R.id.txtEditorial);
        TextView fecha = convertView.findViewById(R.id.txtFecha);
        TextView paginas = convertView.findViewById(R.id.txtPaginas);

        title.setText(currentItem.getTitulo());
        autores.setText(convertArrayToString(currentItem.getAutores()));
        editorial.setText("Editorial: " + currentItem.getEditorial());
        fecha.setText("Fecha de publicación: " + currentItem.getFecha());
        paginas.setText("Número de páginas: " + String.valueOf(currentItem.getPaginas()));
        return convertView;
    }

    private String convertArrayToString(String[] strings){
        String stringAutores = "";
        for (String s : strings){
            stringAutores = stringAutores + "[" + s + "]";
        }
        return stringAutores;
    }
}
