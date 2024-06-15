package com.aluracursos.controller;

import com.aluracursos.dto.EpisodioDTO;
import com.aluracursos.dto.SerieDTO;
import com.aluracursos.repository.SerieRepository;
import com.aluracursos.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SerieController {

    @Autowired
    private SerieService servicio;



    //metodo para obtener las series, la lista de series en realidad
    @GetMapping()
    public List<SerieDTO> obtenerTodasLasSeries(){
        return servicio.obtenerTodasLasSeries();

    }

    //creando los otros endpoints
    @GetMapping("/top5")
    public List<SerieDTO> obtenerTop5(){
        return servicio.obtenerTop5();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> obtenerLanzamientosMasRecientes(){
        return servicio.obtenerLanzamientosMasRecientes();
    }

    //para mostrar los detalles de cada serie, usaremos un parámetro dinámico
    @GetMapping("/{id}")
    public SerieDTO obtenerPorId(@PathVariable Long id){
        return servicio.obtenerPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obtenerTodasLasTemporadas(@PathVariable Long id){
        return servicio.obtenerTodasLasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numeroTemporada}")
    public List<EpisodioDTO> obtenerTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numeroTemporada){
        return servicio.obtenerTemporadasPorNumero(id,numeroTemporada);
    }

    @GetMapping("/categoria/{nombreGenero}")
    public List<SerieDTO>obtenerSeriesPorCategoria(@PathVariable String nombreGenero){
        return servicio.obtenerSeriesPorCategoria(nombreGenero);
    }






    //este era el metodo para devolver el mensaje
    /*@GetMapping("/series")
    public String mostrarMensaje(){
        return "Mi primer mensaje en mi app web";
    }*/
}
