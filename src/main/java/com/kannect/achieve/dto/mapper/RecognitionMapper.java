package com.kannect.achieve.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.kannect.achieve.dto.RecognitionDTO;
import com.kannect.achieve.entity.Badge;
import com.kannect.achieve.entity.Recognition;
import com.kannect.achieve.exception.ResourceNotFoundException;
import com.kannect.achieve.repository.BadgeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecognitionMapper {
	
	private final BadgeRepository badgeRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public Recognition mapToRecognition(RecognitionDTO recognitionDTO) {
		Recognition recognition= modelMapper.map(recognitionDTO, Recognition.class);
		Optional<Badge> badgeOptional = badgeRepository.findById(recognitionDTO.getBadgeId());
		if (badgeOptional.isEmpty()) {
			throw new ResourceNotFoundException("Badge not found for id: " + recognitionDTO.getBadgeId());
		}
		recognition.setBadge(badgeOptional.get());
		return recognition;

	}

	public RecognitionDTO mapToRecognitionDTO(Recognition recognition) {
		RecognitionDTO recognitionDTO= modelMapper.map(recognition, RecognitionDTO.class);
		recognitionDTO.setBadgeId(recognition.getBadge().getId());
		return recognitionDTO;
	}

	public List<RecognitionDTO> mapToRecognitionDTOs(List<Recognition> recognitions) {
		List<RecognitionDTO> dtos = new ArrayList<>();
		for (Recognition recognition : recognitions) {
			dtos.add(mapToRecognitionDTO(recognition));
		}
		return dtos;
	}

	public Recognition mapToRecognition(Recognition recognition, RecognitionDTO recognitionDTO) {
		modelMapper.map(recognitionDTO, recognition);
		Optional<Badge> badgeOptional = badgeRepository.findById(recognitionDTO.getBadgeId());
		if (badgeOptional.isEmpty()) {
			throw new ResourceNotFoundException("Badge not found for id: " + recognitionDTO.getBadgeId());
		}
		recognition.setBadge(badgeOptional.get());
		return recognition;
	}

}
