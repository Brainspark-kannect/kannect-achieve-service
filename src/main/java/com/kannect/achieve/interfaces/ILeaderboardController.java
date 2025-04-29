package com.kannect.achieve.interfaces;

import org.springframework.http.ResponseEntity;

import com.kannect.achieve.dto.response.ErrorResponse;
import com.kannect.achieve.dto.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@Tag(name = "Leaderboard", description = "Leaderboard API")
public interface ILeaderboardController {

	@Operation(summary = "Get leaderboard by period", description = "Retrieve leaderboard entries for a specific period type (WEEKLY, MONTHLY, QUARTERLY, YEARLY)")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
	    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	ResponseEntity<SuccessResponse> getLeaderboardByPeriod(
	   @NotNull @Parameter(description = "Period type (WEEKLY, MONTHLY, QUARTERLY, YEARLY)") String periodType
	);

}
