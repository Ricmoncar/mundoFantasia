package com.daw.controllers;

import java.sql.Date;

public class Libro {
    private Long id;
    private String descripcion;
    private String titulo;
    private Boolean favorite;
    private Date fechaPublicacion;
    private String isbn;
    private String idioma;

    // Constructor
    public Libro(Long id, String descripcion, String titulo, Boolean favorite, 
                 Date fechaPublicacion, String isbn, String idioma) {
        this.id = id;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.favorite = favorite;
        this.fechaPublicacion = fechaPublicacion;
        this.isbn = isbn;
        this.idioma = idioma;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Boolean getFavorite() { return favorite; }
    public void setFavorite(Boolean favorite) { this.favorite = favorite; }

    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
}
