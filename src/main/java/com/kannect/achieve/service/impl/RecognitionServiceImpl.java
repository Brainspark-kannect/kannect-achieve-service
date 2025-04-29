package com.kannect.achieve.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kannect.achieve.dto.RecognitionDTO;
import com.kannect.achieve.dto.mapper.RecognitionMapper;
import com.kannect.achieve.entity.Recognition;
import com.kannect.achieve.repository.RecognitionRepository;
import com.kannect.achieve.service.EmailService;
import com.kannect.achieve.service.RecognitionService;
import com.kannect.achieve.utils.TimePeriodUtil;
import com.kannect.user.auth.entity.User;
import com.kannect.user.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecognitionServiceImpl implements RecognitionService {

	private final RecognitionRepository recognitionRepository;
	private final RecognitionMapper recognitionMapper;
	public static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
	private final UserRepository userRepository;
	private final EmailService emailService;

	@Override
	public RecognitionDTO giveRecognition(RecognitionDTO recognitionDTO) {

		Recognition recognition = recognitionMapper.mapToRecognition(recognitionDTO);

		// Get the start of the current week (Monday)
		LocalDate today = LocalDate.now();
		LocalDate monday = today.with(DayOfWeek.MONDAY);
		LocalDateTime weekStart = monday.atStartOfDay();

		// Check if user has already given 3 recognitions this week
		int givenCount = recognitionRepository.countByGiverIdAndDateGivenAfter(recognition.getGiverId(), weekStart);
		if (givenCount >= 3) {
			throw new RuntimeException("You have already given 3 badges this week.");
		}

		// Check if the same badge was already given to this receiver this week
		boolean alreadyGivenSameBadge = recognitionRepository.existsByReceiverIdAndBadgeIdAndDateGivenAfter(
				recognition.getReceiverId(), recognition.getBadge().getId(), weekStart);
		if (alreadyGivenSameBadge) {
			throw new RuntimeException("This badge has already been given to this user this week.");
		}

		recognition.setDateGiven(LocalDateTime.now());
		recognition.setApproved(false);

		User giver = userRepository.findById(recognition.getGiverId())
				.orElseThrow(() -> new RuntimeException("Giver not found"));
		User receiver = userRepository.findById(recognition.getReceiverId())
				.orElseThrow(() -> new RuntimeException("Receiver not found"));

		String subject = "🎉 You've Received a Recognition!";
		String body = "Hi " + receiver.getFirstName() + ",\n\n" + "You've just been recognized by "
				+ giver.getFirstName() + "!\n" + "Badge: " + recognition.getBadge().getBadgeType() + "\n"
				+ "Message: \"" + recognition.getMessage() + "\"\n\n" + "Keep up the great work!";

		emailService.sendEmail(List.of(receiver.getEmail()), List.of(giver.getEmail()), subject, body);

		return recognitionMapper.mapToRecognitionDTO(recognitionRepository.save(recognition));
	}

	@Override
	public RecognitionDTO approveRecognition(Long recognitionId) {
		Recognition recognition = recognitionRepository.findById(recognitionId)
				.orElseThrow(() -> new RuntimeException("Recognition not found"));

		recognition.setApproved(true);
		User giver = userRepository.findById(recognition.getGiverId())
				.orElseThrow(() -> new RuntimeException("Giver not found"));
		User receiver = userRepository.findById(recognition.getReceiverId())
				.orElseThrow(() -> new RuntimeException("Receiver not found"));

		String subject = "✅ Your Recognition Was Approved!";
		String body = "Hi " + receiver.getFirstName() + ",\n\n" + "The recognition you received from "
				+ giver.getFirstName() + " has been approved by HR.\n\n" + "Badge: "
				+ recognition.getBadge().getBadgeType() + "\n" + "Message: \"" + recognition.getMessage() + "\"\n\n"
				+ "Congrats!";

		emailService.sendEmail(List.of(receiver.getEmail()), List.of(giver.getEmail()), subject, body);

		return recognitionMapper.mapToRecognitionDTO(recognitionRepository.save(recognition));
	}

	@Override
	public List<RecognitionDTO> getApprovedRecognitions() {
		return recognitionMapper.mapToRecognitionDTOs(
				recognitionRepository.findAll().stream().filter(Recognition::getApproved).collect(Collectors.toList()));
	}

	@Override
	public List<RecognitionDTO> getRecognitionsReceivedByUser(Long userId, String period) {
		Pair<LocalDateTime, LocalDateTime> range = TimePeriodUtil.getCurrentPeriodBounds(period);
		return recognitionMapper.mapToRecognitionDTOs(
				recognitionRepository.findReceivedByUserWithinPeriod(userId, range.getLeft(), range.getRight()));
	}

	@Override
	public List<RecognitionDTO> getRecognitionsGivenByUser(Long userId, String period) {
		Pair<LocalDateTime, LocalDateTime> range = TimePeriodUtil.getCurrentPeriodBounds(period);
		return recognitionMapper.mapToRecognitionDTOs(
				recognitionRepository.findGivenByUserWithinPeriod(userId, range.getLeft(), range.getRight()));
	}

	@Override
	public List<RecognitionDTO> getApprovedRecognitions(String period) {
		Pair<LocalDateTime, LocalDateTime> range = TimePeriodUtil.getCurrentPeriodBounds(period);
		return recognitionMapper.mapToRecognitionDTOs(
				recognitionRepository.findApprovedWithinPeriod(range.getLeft(), range.getRight()));
	}
}
