package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.dto.CareerDTO;
import com.vocacional.orientacionvocacional.model.entity.Career;
import com.vocacional.orientacionvocacional.model.entity.Location;
import com.vocacional.orientacionvocacional.model.entity.TestResult;
import com.vocacional.orientacionvocacional.repository.CareerRepository;
import com.vocacional.orientacionvocacional.repository.LocationRepository;
import com.vocacional.orientacionvocacional.repository.TestResultRepository;
import com.vocacional.orientacionvocacional.service.impl.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/career")
public class CareerController {

    @Autowired
    private CareerService careerService;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TestResultRepository testResultRepository;


    @GetMapping("/by-user/{id}")
    public ResponseEntity<List<CareerDTO>> getCareersByUserId(@PathVariable Long id) {
        Optional<TestResult> optionalTestResult = testResultRepository.findByUserId(id);
        if (optionalTestResult.isPresent()) {
            TestResult testResult = optionalTestResult.get();
            Long areaId = testResult.getRecommendedArea().getId();

            List<Career> careers = careerRepository.findByAreaId(areaId);
            List<CareerDTO> careerDTOs = careers.stream()
                    .map(career -> new CareerDTO(career.getId(), career.getName(),career.getDescription(),   career.getPriceMonthly(), career.getImg(),career.getLocation(), career.getArea().getName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(careerDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // O podrías devolver un mensaje vacío
        }
    }


    @GetMapping("/filterExactLocation")
    public ResponseEntity<List<Career>> filterCareersByLocation(
            @RequestParam String city,
            @RequestParam String region,
            @RequestParam String country
    ) {
        List<Career> careers = careerService.getCareersByLocation(city, region, country);
        if (careers != null && !careers.isEmpty()) {
            return ResponseEntity.ok(careers);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @PostMapping("/addLocation")
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        Location newLocation = locationRepository.save(location);
        return ResponseEntity.ok(newLocation);
    }


    @PostMapping("/addCareer")
    public ResponseEntity<Career> addCareer(@RequestParam String nameCareer, @RequestParam Long locationId, @RequestParam String img, @RequestParam String description, @RequestParam String price) {
        Location location = locationRepository.findById(locationId).orElse(null);
        if (location == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Career career = new Career();
        career.setName(nameCareer);
        career.setLocation(location);
        career.setImg(img);
        career.setDescription(description);
        career.setPriceMonthly(price);

        Career newCareer = careerRepository.save(career);
        return ResponseEntity.ok(newCareer);
    }

    @GetMapping("/byLocation/{locationId}")
    public ResponseEntity<List<Career>> getCareersByLocationId(@PathVariable Long locationId) {
        Location location = locationRepository.findById(locationId).orElse(null);
        if (location == null) {
            return ResponseEntity.badRequest().body(null); // Si no se encuentra la ubicación
        }

        List<Career> careers = careerRepository.findByLocation(location);
        return ResponseEntity.ok(careers);
    }


    @GetMapping("/showLocations")
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> locations = locationRepository.findAll();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/showCareers")
    public ResponseEntity<List<Career>> getCareers(){
        List<Career> careers = careerRepository.findAll();
        return ResponseEntity.ok(careers);
    }

    @GetMapping("/CareersByID/{careerId}")
    public ResponseEntity<?> getCareersByID(@PathVariable Long careerId) {
        try {
            Career career = careerService.getCareerById(careerId);
            return ResponseEntity.ok(career);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el usuario: " + e.getMessage());
        }
    }
}