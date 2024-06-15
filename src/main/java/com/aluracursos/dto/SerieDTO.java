package com.aluracursos.dto;

import com.aluracursos.model.Categoria;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO (Long id,
                        String titulo,
        Integer totalDeTemporadas,
         Double evaluacion,
         Categoria genero,
         String sinopsis,
         String poster,
         String actores) {
}
