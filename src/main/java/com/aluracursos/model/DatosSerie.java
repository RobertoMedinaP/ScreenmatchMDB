package com.aluracursos.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//esta anotacion no toma en cuenta el json completo, ignora lo no indicado en esta clase
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
        @JsonAlias ("Title") String titulo,
        @JsonAlias ("totalSeasons") Integer totalDeTemporadas,
        @JsonAlias ("imdbRating") String evaluacion,
        @JsonAlias ("Genre") String genero,
        @JsonAlias("Plot") String sinopsis,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Actors") String actores){
}

//JsonAlias nos permite leer el contenido de la api para pasarlo a un tipo de nuestro programa
//JsonProperty permite tanto leer como escribir, por ej para enviar datos usariamos JsonProperty
//este record lo que hace es mapear parte de la API a valores de nuestros objetos
