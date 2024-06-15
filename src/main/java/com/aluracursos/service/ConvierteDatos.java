package com.aluracursos.service;

import com.aluracursos.model.DatosSerie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    //creamos un object mapper (de la biblioteca jackson)
    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    //dara error, por eso añadimos las excepciones con try catch
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
