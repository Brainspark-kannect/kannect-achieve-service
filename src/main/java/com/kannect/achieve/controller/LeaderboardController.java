package com.kannect.achieve.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kannect.achieve.dto.response.SuccessResponse;
import com.kannect.achieve.entity.LeaderboardEntry;
import com.kannect.achieve.service.LeaderboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
public class LeaderboardController {

	private final LeaderboardService leaderboardService;

	@GetMapping("/{periodType}")
	public ResponseEntity<SuccessResponse> getLeaderboardByPeriod(@PathVariable String periodType) {
		List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboardByPeriod(periodType);
		SuccessResponse successResponse = SuccessResponse.builder().statusCode(200).status(HttpStatus.OK)
				.message("Leaderboard fetched successfully").data(leaderboard).build();

		return ResponseEntity.ok(successResponse);
	}

}
