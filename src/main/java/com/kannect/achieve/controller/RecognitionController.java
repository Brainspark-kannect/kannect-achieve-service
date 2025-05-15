package com.kannect.achieve.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kannect.achieve.dto.RecognitionDTO;
import com.kannect.achieve.dto.response.SuccessResponse;
import com.kannect.achieve.interfaces.IRecognitionController;
import com.kannect.achieve.service.RecognitionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recognitions")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
public class RecognitionController implements IRecognitionController {

    private final RecognitionService recognitionService;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    public ResponseEntity<SuccessResponse> giveRecognition(@RequestBody RecognitionDTO recognitionDTO) {
        RecognitionDTO recognitionResponse = recognitionService.giveRecognition(recognitionDTO);

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Recognition given successfully")
                .data(recognitionResponse)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @Override
    @PutMapping("/approve/{recognitionId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse> approveRecognition(@PathVariable Long recognitionId) {
        RecognitionDTO recognitionResponse = recognitionService.approveRecognition(recognitionId);

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Recognition approved successfully")
                .data(recognitionResponse)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @Override
    @GetMapping("/approved")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<SuccessResponse> getApprovedRecognitions() {
        List<RecognitionDTO> recognitionDTOs = recognitionService.getApprovedRecognitions();

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Approved recognitions fetched successfully")
                .data(recognitionDTOs)
                .build();

        return ResponseEntity.ok(successResponse);
    }
    
    @Override
    @GetMapping("/not/approved")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse> getNotApprovedRecognitions() {
        List<RecognitionDTO> recognitionDTOs = recognitionService.getNotApprovedRecognitions();

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Approved recognitions fetched successfully")
                .data(recognitionDTOs)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @Override
    @GetMapping("/received/{userId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    public ResponseEntity<SuccessResponse> getRecognitionsReceivedByUser(@PathVariable Long userId, @RequestParam String period) {
        List<RecognitionDTO> recognitionDTOs = recognitionService.getRecognitionsReceivedByUser(userId, period);

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Recognitions received by user fetched successfully")
                .data(recognitionDTOs)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @Override
    @GetMapping("/given/{userId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    public ResponseEntity<SuccessResponse> getRecognitionsGivenByUser(@PathVariable Long userId, @RequestParam String period) {
        List<RecognitionDTO> recognitionDTOs = recognitionService.getRecognitionsGivenByUser(userId, period);

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Recognitions given by user fetched successfully")
                .data(recognitionDTOs)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @Override
    @GetMapping("/approved/{period}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse> getApprovedRecognitions(@PathVariable String period) {
        List<RecognitionDTO> recognitionDTOs = recognitionService.getApprovedRecognitions(period);

        SuccessResponse successResponse = SuccessResponse.builder()
                .statusCode(200)
                .status(HttpStatus.OK)
                .message("Approved recognitions for the period fetched successfully")
                .data(recognitionDTOs)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
