package com.example.ricindigus.booklistingapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    EditText parametro;
    Button btnBuscar;
    TextView textView;
    ListaAdapter listaAdapter;
    ProgressBar progressBar;

    final String BASE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista_libros);
        parametro = findViewById(R.id.parametro_busqueda);
        btnBuscar = findViewById(R.id.btn_buscar);
        textView = findViewById(R.id.emptyView);
        progressBar = findViewById(R.id.loading_progress);

        textView.setText(R.string.emptyview);
        lista.setEmptyView(textView);
        listaAdapter = new ListaAdapter(this,new ArrayList<Libro>());
        lista.setAdapter(listaAdapter);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentParameter = parametro.getText().toString();
                Uri baseUri = Uri.parse(BASE_REQUEST_URL);
                Uri.Builder uriBuilder = baseUri.buildUpon();
                uriBuilder.appendQueryParameter("q",currentParameter);
                uriBuilder.appendQueryParameter("orderBy","newest");
                uriBuilder.appendQueryParameter("maxResults","12");
                new BooksAsynctask().execute(uriBuilder.toString());
            }
        });

    }

    private class BooksAsynctask extends AsyncTask<String,Void,List<Libro>>{

        @Override
        protected void onPreExecute() {
            textView.setText("");
            listaAdapter.clear();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Libro> doInBackground(String... stringUrl) {

            List<Libro> libros = new ArrayList<>();

            libros = HttpUtils.fetchBooksData(stringUrl[0]);

            return libros;
        }

        @Override
        protected void onPostExecute(List<Libro> libros) {
            progressBar.setVisibility(View.GONE);
            if (libros.size() == 0) textView.setText("No hay resultados para mostrar. Realice una búsqueda");
            listaAdapter.clear();
            listaAdapter = new ListaAdapter(MainActivity.this,libros);
            lista.setAdapter(listaAdapter);
        }
    }
}
