package com.vocacional.orientacionvocacional.Mapper;

import com.vocacional.orientacionvocacional.dto.AdviserDTO;
import com.vocacional.orientacionvocacional.model.entity.Adviser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AdviserMapper {

    private final ModelMapper modelMapper;

    public AdviserMapper(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    public AdviserDTO toDTO(Adviser asesor){
        return modelMapper.map(asesor, AdviserDTO.class);
    }

    public Adviser toEntity(AdviserDTO adviserDTO){
        return modelMapper.map(adviserDTO, Adviser.class);
    }
}
