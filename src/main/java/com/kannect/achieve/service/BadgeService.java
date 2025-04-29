package com.kannect.achieve.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kannect.achieve.dto.BadgeDTO;
import com.kannect.achieve.exception.RequestValidationFailedException;

public interface BadgeService {

	BadgeDTO createBadge(BadgeDTO badgeDTO, MultipartFile badgePhoto) throws RequestValidationFailedException, IOException;

	List<BadgeDTO> getAllBadges();

	BadgeDTO getBadgeById(Long id);

	BadgeDTO getBadgeByType(String type);

	BadgeDTO updateBadge(Long badgeId, BadgeDTO badgeDTO, MultipartFile badgePhoto)
			throws RequestValidationFailedException, IOException;

}
