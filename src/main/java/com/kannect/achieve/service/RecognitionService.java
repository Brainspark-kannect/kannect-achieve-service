package com.kannect.achieve.service;

import java.util.List;

import com.kannect.achieve.dto.RecognitionDTO;

public interface RecognitionService {

	List<RecognitionDTO> getApprovedRecognitions(String period);

	List<RecognitionDTO> getRecognitionsGivenByUser(Long userId, String period);

	List<RecognitionDTO> getRecognitionsReceivedByUser(Long userId, String period);

	List<RecognitionDTO> getApprovedRecognitions();

	RecognitionDTO approveRecognition(Long recognitionId);

	RecognitionDTO giveRecognition(RecognitionDTO recognitionDTO);

}
