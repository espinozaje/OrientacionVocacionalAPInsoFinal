package com.vocacional.orientacionvocacional.repository;

import com.vocacional.orientacionvocacional.model.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
List<Availability> findByAdviserId(Integer adviserId);
    @Transactional
    void deleteByAdviserId(Integer adviserId);


    Optional<Availability> findByAdviserIdAndDayOfWeek(int adviserId, String dayOfWeek);
    boolean existsByAdviserIdAndDayOfWeek(Integer adviserId, String dayOfWeek);


}
