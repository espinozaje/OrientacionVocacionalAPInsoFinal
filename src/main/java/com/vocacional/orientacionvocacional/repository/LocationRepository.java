package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionRepository extends JpaRepository<Location, Long> {
    Location findByCiudadAndRegionAndPais(String ciudad, String region, String pais);
}