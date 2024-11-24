package com.vocacional.orientacionvocacional.Mapper;

import com.vocacional.orientacionvocacional.dto.QuestionDTO;
import com.vocacional.orientacionvocacional.model.entity.Question;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    private final ModelMapper modelMapper;

    public QuestionMapper(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    public QuestionDTO toDTO(Question question){
        QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);

        return questionDTO;
    }

    public Question toEntity(QuestionDTO questionDTO){
        return modelMapper.map(questionDTO, Question.class);
    }
}
