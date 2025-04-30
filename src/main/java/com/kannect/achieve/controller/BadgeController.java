package com.kannect.achieve.controller;


import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kannect.achieve.dto.BadgeDTO;
import com.kannect.achieve.dto.response.SuccessResponse;
import com.kannect.achieve.interfaces.IBadgeController;
import com.kannect.achieve.service.BadgeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/badges")
@RequiredArgsConstructor
@CrossOrigin
public class BadgeController implements IBadgeController {

	private final BadgeService badgeService;
	public static final Logger LOGGER = LoggerFactory.getLogger(BadgeController.class);

	@Override
	@PostMapping
	@PreAuthorize("hasAnyRole('HR', 'ADMIN')")
	public ResponseEntity<SuccessResponse> createBadge(@RequestPart("data") String badgeDTOStr,
			@RequestPart("file") MultipartFile badgePhoto) throws Exception, IOException {
		ObjectMapper objectmapper = new ObjectMapper();
		BadgeDTO badgeDTO = new BadgeDTO();
		try {
			badgeDTO = objectmapper.readValue(badgeDTOStr, BadgeDTO.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error mapping json String to BadgeDTO while adding");
		}
		BadgeDTO createdBadge = badgeService.createBadge(badgeDTO, badgePhoto);

		SuccessResponse successResponse = SuccessResponse.builder().statusCode(201).status(HttpStatus.CREATED)
				.message("Badge created successfully").data(createdBadge).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);

	}

	@Override
	@GetMapping
	@PreAuthorize("hasAnyRole('HR', 'ADMIN', 'EMPLOYEE')")
	public ResponseEntity<SuccessResponse> getAllBadges() {
		List<BadgeDTO> badges = badgeService.getAllBadges();

		SuccessResponse successResponse = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Fetched all badges successfully").data(badges).build();

		return ResponseEntity.ok(successResponse);
	}

	@Override
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('HR', 'ADMIN', 'EMPLOYEE')")
	public ResponseEntity<SuccessResponse> getBadgeById(@PathVariable Long id) {
		BadgeDTO badge = badgeService.getBadgeById(id);

		SuccessResponse successResponse = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Fetched badge by ID successfully").data(badge).build();

		return ResponseEntity.ok(successResponse);
	}

	@Override
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'EMPLOYEE')")
    public ResponseEntity<SuccessResponse> getBadgeByType(@PathVariable String type) {
        BadgeDTO badge = badgeService.getBadgeByType(type);

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Fetched badge by type successfully")
                .data(badge)
                .build();

        return ResponseEntity.ok(successResponse);
    }

	@Override
    @PutMapping("/{badgeId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<SuccessResponse> updateBadge(@PathVariable Long badgeId, @RequestPart("data") final String badgeDTOStr,@RequestPart("file") final  MultipartFile badgePhoto) throws Exception, IOException {
    	ObjectMapper objectmapper = new ObjectMapper();
    	BadgeDTO badgeDTO = new BadgeDTO();
		try {
			badgeDTO = objectmapper.readValue(badgeDTOStr,
					BadgeDTO.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error mapping json String to BadgeDTO while adding");
		}
            BadgeDTO updatedBadge = badgeService.updateBadge(badgeId, badgeDTO, badgePhoto);

            SuccessResponse successResponse = SuccessResponse.builder()
                    .statusCode(200)
                    .status(HttpStatus.OK)
                    .message("Badge updated successfully")
                    .data(updatedBadge)
                    .build();

            return ResponseEntity.ok(successResponse);
       
    }
}
