package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Career;
import com.vocacional.orientacionvocacional.model.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {
    List<Career> findByLocation(Location location);
    List<Career> findByAreaId(Long areaId);
}