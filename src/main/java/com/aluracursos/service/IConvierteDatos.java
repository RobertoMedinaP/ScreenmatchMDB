package com.aluracursos.service;

public interface IConvierteDatos {
    //Las T indican que se trabaja con datos genericos
    <T> T obtenerDatos(String json, Class <T> clase);
}
