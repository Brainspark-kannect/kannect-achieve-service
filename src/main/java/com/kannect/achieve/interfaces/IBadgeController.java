package com.kannect.achieve.interfaces;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.kannect.achieve.dto.response.ErrorResponse;
import com.kannect.achieve.dto.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Badge", description = "Badge Management API")
public interface IBadgeController {

    @Operation(summary = "Create badge", description = "Create a new badge with image")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Badge created successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> createBadge(
        @Parameter(description = "Badge data as JSON string") String badgeDTOStr,
        @Parameter(description = "Badge photo as multipart file") MultipartFile badgePhoto
    ) throws Exception, IOException;

    @Operation(summary = "Get all badges", description = "Retrieve all badges")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Badges retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getAllBadges();

    @Operation(summary = "Get badge by ID", description = "Retrieve badge by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Badge retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "Badge not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getBadgeById(
        @Parameter(description = "Badge ID") Long id
    );

    @Operation(summary = "Update badge", description = "Update badge details and/or image")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Badge updated successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Badge not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> updateBadge(
        @Parameter(description = "Badge ID to update") Long badgeId,
        @Parameter(description = "Updated badge data as JSON string") String badgeDTOStr,
        @Parameter(description = "New badge photo as multipart file (optional)") MultipartFile badgePhoto
    ) throws Exception, IOException;

    @Operation(summary = "Get badges by type", description = "Retrieve badges based on badge type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Badges retrieved successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid type value", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse> getBadgeByType(
        @Parameter(description = "Type of the badge (e.g., Bronze, Silver, Gold)") String type
    );
}
