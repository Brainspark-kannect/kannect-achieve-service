package com.kannect.achieve.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.kannect.achieve.dto.BadgeDTO;
import com.kannect.achieve.entity.Badge;

@Component
public class BadgeMapper {

	private final ModelMapper modelMapper = new ModelMapper();

	public Badge mapToBadge(BadgeDTO badgeDTO) {
		return modelMapper.map(badgeDTO, Badge.class);

	}

	public BadgeDTO mapToBadgeDTO(Badge badge) {
		return modelMapper.map(badge, BadgeDTO.class);
	}

	public List<BadgeDTO> mapToBadgeDTOs(List<Badge> badges) {
		List<BadgeDTO> dtos = new ArrayList<>();
		for (Badge badge : badges) {
			dtos.add(mapToBadgeDTO(badge));
		}
		return dtos;
	}

	public Badge mapToBadge(Badge badge, BadgeDTO badgeDTO) {
		modelMapper.map(badgeDTO, badge);
		return badge;
	}
}
