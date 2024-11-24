package com.vocacional.orientacionvocacional.Mapper;

import com.vocacional.orientacionvocacional.dto.AdviserDTO;
import com.vocacional.orientacionvocacional.dto.AdvisoryDTO;
import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Advisory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AdvisoryMapper {
    private final ModelMapper modelMapper;

    public AdvisoryMapper(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    public AdvisoryDTO toDTO(Advisory advisory) {
        AdvisoryDTO dto = new AdvisoryDTO();
        dto.setId(advisory.getId());
        dto.setLink(advisory.getLink());
        dto.setName(advisory.getName());
        dto.setDate(advisory.getDate());
        dto.setTime(advisory.getTime());

        // Mapear los IDs de student y adviser
        dto.setStudentId(advisory.getStudent() != null ? advisory.getStudent().getId() : null);
        dto.setAdviserId(advisory.getAdviser() != null ? advisory.getAdviser().getId() : null);

        return dto;
    }

    public Advisory toEntity(AdvisoryDTO dto) {
        Advisory advisory = new Advisory();
        advisory.setId(dto.getId());
        advisory.setLink(dto.getLink());
        advisory.setName(dto.getName());
        advisory.setDate(dto.getDate());
        advisory.setTime(dto.getTime());

        return advisory;
    }
}
