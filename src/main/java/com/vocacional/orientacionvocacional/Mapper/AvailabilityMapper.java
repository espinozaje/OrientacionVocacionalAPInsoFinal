package com.vocacional.orientacionvocacional.Mapper;

import com.vocacional.orientacionvocacional.dto.AvailabilityDTO;
import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Availability;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Component
public class AvailabilityMapper {

    public AvailabilityDTO mapToDTO(Availability availability) {
        return new AvailabilityDTO(
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        );
    }

    public Availability mapToEntity(AvailabilityDTO dto, Adviser adviser) {
        Availability availability = new Availability();
        availability.setAdviser(adviser);
        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        return availability;
    }
}
