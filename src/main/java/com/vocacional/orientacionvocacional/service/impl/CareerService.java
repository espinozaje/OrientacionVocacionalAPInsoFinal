package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.model.entity.Career;
import com.vocacional.orientacionvocacional.model.entity.Location;
import com.vocacional.orientacionvocacional.repository.CarreraRepository;
import com.vocacional.orientacionvocacional.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    public List<Career> obtenerCarrerasPorUbicacion(String ciudad, String region, String pais) {
        Location ubicacion = ubicacionRepository.findByCiudadAndRegionAndPais(ciudad, region, pais);
        if (ubicacion != null) {
            return carreraRepository.findByUbicacion(ubicacion);
        }
        return null;
    }

    public Career getCarreraById(Long id) throws Exception {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new Exception("Carrera no encontrada con id: " + id));
    }
}