package com.recipemanagement.serviceimpl;

import com.recipemanagement.dto.NutritionDto;
import com.recipemanagement.entity.NutritionEntity;
import com.recipemanagement.exception.NutritionNotFoundException;
import com.recipemanagement.repository.NutritionRepository;
import com.recipemanagement.service.NutritionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NutritionServiceImpl implements NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<NutritionDto> getNutritions() {
        List<NutritionEntity> nutritions = nutritionRepository.findAll();
        return nutritions.stream().map(nutritionEntity -> modelMapper.map(nutritionEntity, NutritionDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<NutritionDto> getNutrition(String id) throws NutritionNotFoundException {
        Optional<NutritionEntity> nutritionEntity = nutritionRepository.findById(id);
        if (nutritionEntity.isEmpty()) {
            throw new NutritionNotFoundException("Nutrition not found");
        } else {
            NutritionDto nutritionDto = modelMapper.map(nutritionEntity.get(), NutritionDto.class);
            return Optional.of(nutritionDto);
        }
    }

    @Override
    public NutritionDto addNutrition(NutritionDto nutritionDto) {
        NutritionEntity nutritionEntity = modelMapper.map(nutritionDto, NutritionEntity.class);
        NutritionEntity savedNutritionEntity = nutritionRepository.save(nutritionEntity);
        return modelMapper.map(savedNutritionEntity, NutritionDto.class);
    }

    @Override
    public Optional<NutritionDto> updateNutrition(String id, NutritionDto updatedNutritionDto) throws NutritionNotFoundException {
        Optional<NutritionEntity> existingNutritionEntity = nutritionRepository.findById(id);
        if (existingNutritionEntity.isEmpty()) throw new NutritionNotFoundException("Nutrition not found");

        NutritionEntity updatedNutritionEntity = modelMapper.map(updatedNutritionDto, NutritionEntity.class);
        updatedNutritionEntity.setNutritionId(id);
        nutritionRepository.save(updatedNutritionEntity);

        return Optional.of(modelMapper.map(updatedNutritionEntity, NutritionDto.class));
    }

    @Override
    public void deleteNutrition(String id) throws NutritionNotFoundException {
        Optional<NutritionEntity> existingNutritionEntity = nutritionRepository.findById(id);
        if (existingNutritionEntity.isEmpty()) throw new NutritionNotFoundException("Nutrition not found");

        nutritionRepository.deleteById(id);
    }
}
