package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.Mapper.AdviserMapper;
import com.vocacional.orientacionvocacional.dto.AdviserDTO;
import com.vocacional.orientacionvocacional.exception.BadRequestException;
import com.vocacional.orientacionvocacional.exception.ResourceNotFoundException;
import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.enums.ERole;
import com.vocacional.orientacionvocacional.repository.AdviserRepository;
import com.vocacional.orientacionvocacional.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AdviserService {
    @Autowired
    private AdviserRepository adviserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdviserMapper adviserMapper;

    public Optional<Adviser> getAdvisorProfileByEmail(String email) {
        return adviserRepository.findByEmail(email);
    }

    public List<AdviserDTO> listAllAdvisors() {
        List<Adviser> advisors = adviserRepository.findAll();
        return advisors.stream()
                .map(adviserMapper::toDTO)
                .toList();
    }

    public AdviserDTO findById(Long id){
        Adviser adviser = adviserRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("El asesor con ID " + id+ "no fue encontrado"));
        return adviserMapper.toDTO(adviser);
    }


    public AdviserDTO registerAdvisor(AdviserDTO adviserDTO) {
        Integer randomId= generateUniqueRandomId();

        Adviser asesor = adviserMapper.toEntity(adviserDTO);
        ERole eRole = ERole.ADVISER;
        asesor.setId(randomId);
        asesor.setImg_profile("profile.png");
        asesor.setFirstName(adviserDTO.getFirstName());
        asesor.setLastName(adviserDTO.getLastName());
        asesor.setEmail(adviserDTO.getEmail());
        asesor.setSpecialty(adviserDTO.getSpecialty());
        asesor.setPassword(passwordEncoder.encode(adviserDTO.getPassword()));
        asesor.setRole(eRole);
        asesor = userRepository.save(asesor);
        return adviserMapper.toDTO(asesor);
    }


    private Integer generateUniqueRandomId() {
        Random random = new Random();
        Integer randomId;

        do {
            randomId = 1000000 + random.nextInt(9000000);
        } while (adviserRepository.existsById(Long.valueOf(randomId)));

        return randomId;
    }
}

