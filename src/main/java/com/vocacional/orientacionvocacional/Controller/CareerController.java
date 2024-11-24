package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.model.entity.Career;
import com.vocacional.orientacionvocacional.model.entity.Location;
import com.vocacional.orientacionvocacional.repository.CarreraRepository;
import com.vocacional.orientacionvocacional.repository.UbicacionRepository;
import com.vocacional.orientacionvocacional.service.impl.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carreras")
public class CarreraController {

    @Autowired
    private CareerService careerService;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @GetMapping("/filtrarubicacionexacta")
    public ResponseEntity<List<Career>> filtrarCarrerasPorUbicacion(
            @RequestParam String ciudad,
            @RequestParam String region,
            @RequestParam String pais
    ) {
        List<Career> carreras = careerService.obtenerCarrerasPorUbicacion(ciudad, region, pais);
        if (carreras != null && !carreras.isEmpty()) {
            return ResponseEntity.ok(carreras);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @PostMapping("/insertarubi")
    public ResponseEntity<Location> agregarUbicacion(@RequestBody Location ubicacion) {
        Location nuevaUbicacion = ubicacionRepository.save(ubicacion);
        return ResponseEntity.ok(nuevaUbicacion);
    }


    @PostMapping("/insertarcarrera")
    public ResponseEntity<Career> agregarCarrera(@RequestParam String nombreCarrera, @RequestParam Long ubicacionId, @RequestParam String img, @RequestParam String descripcion, @RequestParam String precio) {
        Location ubicacion = ubicacionRepository.findById(ubicacionId).orElse(null);
        if (ubicacion == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Career carrera = new Career();
        carrera.setNombre(nombreCarrera);
        carrera.setUbicacion(ubicacion);
        carrera.setImg(img);
        carrera.setDescripcion(descripcion);
        carrera.setPrecioMensualidad(precio);

        Career nuevaCarrera = carreraRepository.save(carrera);
        return ResponseEntity.ok(nuevaCarrera);
    }

    @GetMapping("/porubicacion/{ubicacionId}")
    public ResponseEntity<List<Career>> obtenerCarrerasPorUbicacion(@PathVariable Long ubicacionId) {
        Location ubicacion = ubicacionRepository.findById(ubicacionId).orElse(null);
        if (ubicacion == null) {
            return ResponseEntity.badRequest().body(null); // Si no se encuentra la ubicación
        }

        List<Career> carreras = carreraRepository.findByUbicacion(ubicacion);
        return ResponseEntity.ok(carreras);
    }


    @GetMapping("/mostrarubicaciones")
    public ResponseEntity<List<Location>> obtenerUbicaciones() {
        List<Location> ubicaciones = ubicacionRepository.findAll();
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/mostrarcarreras")
    public ResponseEntity<List<Career>> obtenercarreras(){
        List<Career> carreras = carreraRepository.findAll();
        return ResponseEntity.ok(carreras);
    }

    @GetMapping("/carreraporID/{carreraId}")
    public ResponseEntity<?> obtenerCarrerasPorID(@PathVariable Long carreraId) {
        try {
            Career carrera = careerService.getCarreraById(carreraId);
            return ResponseEntity.ok(carrera);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el usuario: " + e.getMessage());
        }
    }
}