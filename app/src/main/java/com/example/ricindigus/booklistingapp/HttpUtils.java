package com.example.ricindigus.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {

    public static final String LOG_TAG = HttpUtils.class.getSimpleName().toUpperCase();

    public static URL createURL(String strURL){
        URL url = null;
        try{
            url = new URL(strURL);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG,"Error al crear la url" , e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine(); //lee una linea del stream (leyendo stream)
            while (line != null){
                output.append(line);
                line =bufferedReader.readLine();//leemos la siguiente linea
            }
        }
        return output.toString();
    }

    public static List<Libro> extractFeatureFromJSON(String bookJSON){
        if (TextUtils.isEmpty(bookJSON)) return null;

        List<Libro> libros = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(bookJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectBook = jsonArray.getJSONObject(i);
                JSONObject jsonObjectVolumeInfo = jsonObjectBook.getJSONObject("volumeInfo");

                //  Titulo del libro
                String titulo = jsonObjectVolumeInfo.getString("title");

                //  Autores del libro
                JSONArray jsonArrayAuthors = jsonObjectVolumeInfo.getJSONArray("authors");
                String[] autores = new String[jsonArrayAuthors.length()];
                for (int j = 0; j < jsonArrayAuthors.length(); j++) {
                    autores[j] = jsonArrayAuthors.getString(j);
                }

                //  Editorial del libro
                String editorial = jsonObjectVolumeInfo.getString("publisher");

                //  Fecha de publicación del libro
                String fecha = jsonObjectVolumeInfo.getString("publishedDate");

                //  Numero de paginas del libro
                int paginas = jsonObjectVolumeInfo.getInt("pageCount");

                // Creamos el objeto libro
                Libro libro = new Libro(titulo,autores,editorial,paginas,fecha);

                // Agregamos el nuevo libro a la lista de libros
                libros.add(libro);
            }
        } catch (JSONException e) {
            Log.e("HttpUtils", "Problemas al parsear la respuesta JSONBook", e);
        }


        return libros;
    }

    public static List<Libro> fetchBooksData(String requestUrl){

        URL url = createURL(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Libro> libros = extractFeatureFromJSON(jsonResponse);

        return libros;
    }

}
