package com.kannect.achieve.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kannect.achieve.dto.BadgeDTO;
import com.kannect.achieve.dto.mapper.BadgeMapper;
import com.kannect.achieve.entity.Badge;
import com.kannect.achieve.exception.RequestValidationFailedException;
import com.kannect.achieve.exception.ResourceNotFoundException;
import com.kannect.achieve.repository.BadgeRepository;
import com.kannect.achieve.service.BadgeService;
import com.kannect.achieve.utils.CloudinaryUploader;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

	private final BadgeRepository badgeRepository;
	private final BadgeMapper badgeMapper;
	private final Validator validator;
	private final CloudinaryUploader cloudinaryUploader;
	public static final Logger LOGGER = LoggerFactory.getLogger(BadgeServiceImpl.class);
	
	private static final List<String> VALID_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg",
			"image/gif", "image/webp");

	private static final String FILE_NAME_REGEX = "^[a-zA-Z0-9._-]+$";

	private static final List<String> VALID_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");


	@Override
	public BadgeDTO createBadge(BadgeDTO badgeDTO,MultipartFile badgePhoto) throws RequestValidationFailedException, IOException {
		String badgePhotoUrl = null;
		validateDTOAndFile(badgeDTO, badgePhoto);

		if (badgePhoto != null && !badgePhoto.isEmpty()) {
			String fileName = "badge-photos/" + UUID.randomUUID() + "-" + badgePhoto.getOriginalFilename();
			badgePhotoUrl = cloudinaryUploader.uploadFile(badgePhoto, fileName);
		}

		Badge badge = badgeMapper.mapToBadge(badgeDTO);
		badge.setBadgeImageUrl(badgePhotoUrl);
		return badgeMapper.mapToBadgeDTO(badgeRepository.save(badge));
	}
	
	public <T> void validateDTOAndFile(T dto, MultipartFile multipartFile) throws RequestValidationFailedException {

		// 1. Validate DTO
		Set<ConstraintViolation<T>> violations = validator.validate(dto);
		if (!violations.isEmpty()) {
			Map<String, String> errors = new HashMap<>();
			for (ConstraintViolation<T> violation : violations) {
				errors.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			LOGGER.error("DTO validation failed: {}", errors);
			throw new RequestValidationFailedException(errors.toString());
		}

		// 2. Validate File (if present)
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String contentType = multipartFile.getContentType();
			String originalFilename = multipartFile.getOriginalFilename();

			if (StringUtils.isBlank(contentType) || !VALID_IMAGE_TYPES.contains(contentType)) {
				LOGGER.error("File validation failed: Invalid content type: {}", contentType);
				throw new RequestValidationFailedException("Invalid file type: " + contentType);
			}

			if (StringUtils.isBlank(originalFilename) || !originalFilename.matches(FILE_NAME_REGEX)) {
				LOGGER.error("File validation failed: Invalid filename: {}", originalFilename);
				throw new RequestValidationFailedException("Invalid file name: " + originalFilename);
			}

			// 2. Validate file extension
			String fileExtension = getFileExtension(originalFilename);
			if (StringUtils.isBlank(fileExtension)
					|| VALID_IMAGE_EXTENSIONS.stream().noneMatch(ext -> ext.equalsIgnoreCase(fileExtension))) {

				LOGGER.error("File validation failed: Invalid file extension: {}", fileExtension);
				throw new RequestValidationFailedException("Invalid file extension: " + fileExtension);
			}

		}
	}
	
	private String getFileExtension(String filename) {
		if (StringUtils.isBlank(filename) || !filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
	}

	

	@Override
	public List<BadgeDTO> getAllBadges() {
		return badgeMapper.mapToBadgeDTOs(badgeRepository.findAll());
	}

	@Override
	public BadgeDTO getBadgeById(Long id) {
		Optional<Badge> badgeOptional = badgeRepository.findById(id);
		if (badgeOptional.isEmpty()) {
			LOGGER.error("Badge not found for id: " + id);
			throw new ResourceNotFoundException("Badge not found for id: " + id);
		}

		return badgeMapper.mapToBadgeDTO(badgeOptional.get());
	}

	@Override
	public BadgeDTO getBadgeByType(String type) {
		Optional<Badge> badgeOptional = badgeRepository.findByBadgeType(type);
		if (badgeOptional.isEmpty()) {
			LOGGER.error("Badge not found for type: " + type);
			throw new ResourceNotFoundException("Badge not found for type: " + type);
		}

		return badgeMapper.mapToBadgeDTO(badgeOptional.get());
	}

	

	@Override
	public BadgeDTO updateBadge(Long badgeId, BadgeDTO badgeDTO,MultipartFile badgePhoto) throws RequestValidationFailedException, IOException {
		Badge badge = badgeRepository.findById(badgeId).orElseThrow(() -> new RuntimeException("Badge not found"));
		validateDTOAndFile(badgeDTO, badgePhoto);
		String badgePhotoUrl = badge.getBadgeImageUrl();

		if (badgePhoto != null && !badgePhoto.isEmpty()) {
			String fileName = "badge-photos/" + UUID.randomUUID() + "-" + badgePhoto.getOriginalFilename();
			badgePhotoUrl = cloudinaryUploader.uploadFile(badgePhoto, fileName);
		}
		badge = badgeMapper.mapToBadge(badge, badgeDTO);
		badge.setBadgeImageUrl(badgePhotoUrl);
		return badgeMapper.mapToBadgeDTO(badgeRepository.save(badge));
	}

	

}
