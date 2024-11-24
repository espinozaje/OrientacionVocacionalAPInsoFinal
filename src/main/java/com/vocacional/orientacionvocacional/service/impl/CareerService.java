package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.model.entity.Career;
import com.vocacional.orientacionvocacional.model.entity.Location;
import com.vocacional.orientacionvocacional.repository.CareerRepository;
import com.vocacional.orientacionvocacional.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerService {

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<Career> getCareersByLocation(String ciudad, String region, String pais) {
        Location location = locationRepository.findByCityAndRegionAndCountry(ciudad, region, pais);
        if (location != null) {
            return careerRepository.findByLocation(location);
        }
        return null;
    }

    public Career getCareerById(Long id) throws Exception {
        return careerRepository.findById(id)
                .orElseThrow(() -> new Exception("Carrera no encontrada con id: " + id));
    }
}