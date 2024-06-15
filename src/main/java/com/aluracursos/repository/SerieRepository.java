package com.aluracursos.repository;

import com.aluracursos.dto.EpisodioDTO;
import com.aluracursos.model.Categoria;
import com.aluracursos.model.Episodio;
import com.aluracursos.model.Serie;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository <Serie, Long> {

    //vamos a encontrar el texto buscado en principal
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    //Para encontrar el top5 de las series
    List <Serie> findTop5ByOrderByEvaluacionDesc();
    //para buscar por categoria, la variable es genero pero el findby espera un enum de categoria
    List<Serie> findByGenero(Categoria categoria);
    //Para buscar por cantidad de temporadas
    //todo revisar bien la documentacion de JPA
    //List<Serie> findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, Double evaluacion);
    //La linea anterior funciona, pero ahora trataremos de hacerlo como una query nativa
    //aca buscamos segun la clase, s que es una serie s y de ahi buscamos por los atributos de la serie s, para eso los :
    @Query ("SELECT s FROM Serie s WHERE s.totalDeTemporadas <= :totalTemporadas and s.evaluacion>= :evaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(int totalTemporadas, Double evaluacion);

    //para buscar por partes del título
    //@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE LOWER (e.titulo) LIKE LOWER(CONCAT('%', :nombreEpisodio, '%')) ")
    //List <Episodio> episodiosPorNombre(@Param("nombreEpisodio") String nombreEpisodio);

    @Query("SELECT e FROM Episodio e JOIN FETCH e.serie WHERE LOWER(e.titulo) LIKE LOWER(CONCAT('%', :nombreEpisodio, '%'))")
    List<Episodio> episodiosPorNombre(@Param("nombreEpisodio") String nombreEpisodio);

    @Query("SELECT e FROM Episodio e JOIN FETCH e.serie s WHERE s = :serie ORDER BY e.evaluacion DESC")
    List<Episodio>top5Episodios(Serie serie);

    //metodo para obtener los episodios mas recientes por serie con jpql

    @Query("SELECT s FROM Serie s " + "JOIN s.episodios e " + "GROUP BY s " + "ORDER BY MAX(e.fechaDeLanzamiento) DESC")
    List<Serie> lanzamientosMasRecientes();


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numeroTemporada")
    List<Episodio> obtenerTemporadasPorNumero(Long id, Long numeroTemporada);
}
