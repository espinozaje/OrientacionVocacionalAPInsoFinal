package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.Mapper.AdminMapper;
import com.vocacional.orientacionvocacional.dto.AdminDTO;
import com.vocacional.orientacionvocacional.model.entity.Admin;
import com.vocacional.orientacionvocacional.model.enums.ERole;
import com.vocacional.orientacionvocacional.repository.AdminRepository;
import com.vocacional.orientacionvocacional.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AdminService {


    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminService(AdminMapper adminMapper, PasswordEncoder passwordEncoder, UserRepository userRepository, AdminRepository adminRepository) {
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public AdminDTO registerAdvisor(AdminDTO adminDTO) {
        Integer randomId= generateUniqueRandomId();

        Admin admin = adminMapper.toEntity(adminDTO);
        ERole eRole = ERole.ADMIN;
        admin.setId(randomId);
        admin.setImg_profile("profile.png");
        admin.setFirstName(adminDTO.getFirstName());
        admin.setLastName(adminDTO.getLastName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setRole(eRole);
        admin = userRepository.save(admin);
        return adminMapper.toDTO(admin);
    }


    private Integer generateUniqueRandomId() {
        Random random = new Random();
        Integer randomId;

        do {
            randomId = 1000000 + random.nextInt(9000000);
        } while (
                adminRepository.existsById(Long.valueOf(randomId)));


        return randomId;
    }
}
