package com.example.ricindigus.booklistingapp;

public class Libro {
    private String titulo;
    private String[] autores;
    private String editorial;
    private int paginas;
    private String fecha;

    public Libro(String titulo, String[] autores, String editorial, int paginas, String fecha) {
        this.titulo = titulo;
        this.autores = autores;
        this.editorial = editorial;
        this.paginas = paginas;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String[] getAutores() {
        return autores;
    }

    public String getEditorial() {
        return editorial;
    }

    public int getPaginas() {
        return paginas;
    }

    public String getFecha() {
        return fecha;
    }
}
