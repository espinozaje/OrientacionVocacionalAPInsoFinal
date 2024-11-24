package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.dto.AvailabilityDTO;
import com.vocacional.orientacionvocacional.model.entity.Availability;
import com.vocacional.orientacionvocacional.service.impl.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping("/{adviserId}")
    public ResponseEntity<List<AvailabilityDTO>> getAvailabilities(@PathVariable Long adviserId) {
        return ResponseEntity.ok(availabilityService.getAvailabilities(adviserId));
    }

    @PutMapping("/{adviserId}")
    public ResponseEntity<Void> updateAvailabilities(
            @PathVariable Long adviserId,
            @RequestBody @Valid List<AvailabilityDTO> availabilityDTOs) {
        availabilityService.updateAvailabilities(adviserId, availabilityDTOs);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{adviserId}/{dayOfWeek}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long adviserId, @PathVariable String dayOfWeek) {
        availabilityService.deleteAvailability(adviserId, dayOfWeek);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{adviserId}")
    public ResponseEntity<Availability> addAvailability(
            @PathVariable Long adviserId,
            @RequestBody @Valid AvailabilityDTO availabilityDto) {
        Availability availability = availabilityService.addAvailability(adviserId, availabilityDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(availability);
    }
}
