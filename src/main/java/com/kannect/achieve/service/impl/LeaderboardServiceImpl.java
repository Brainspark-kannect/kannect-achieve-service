package com.kannect.achieve.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kannect.achieve.entity.LeaderboardEntry;
import com.kannect.achieve.repository.LeaderboardRepository;
import com.kannect.achieve.repository.RecognitionRepository;
import com.kannect.achieve.repository.TaskRepository;
import com.kannect.achieve.service.EmailService;
import com.kannect.achieve.service.LeaderboardService;
import com.kannect.achieve.utils.TimePeriodUtil;
import com.kannect.user.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

	private final TaskRepository taskRepository;
	private final RecognitionRepository recognitionRepository;
	private final LeaderboardRepository leaderboardRepository;
	private final UserRepository userRepository;
	private final EmailService emailService;

	private final Logger LOGGER = LoggerFactory.getLogger(LeaderboardServiceImpl.class);

	@Override
	@Scheduled(cron = "0 0 18 ? * FRI")
	public void generateWeeklyLeaderboard() {
		generateLeaderboard("WEEKLY", TimePeriodUtil.getCurrentWeekBounds());
	}

	@Override
	@Scheduled(cron = "0 0 18 L * ?")
	public void generateMonthlyLeaderboard() {
		generateLeaderboard("MONTHLY", TimePeriodUtil.getCurrentMonthBounds());
	}

	@Override
	@Scheduled(cron = "0 0 18 31 3,6,9,12 ?")
	public void generateQuarterlyLeaderboard() {
		generateLeaderboard("QUARTERLY", TimePeriodUtil.getCurrentQuarterBounds());
	}

	@Override
	@Scheduled(cron = "0 0 18 31 12 ?")
	public void generateYearlyLeaderboard() {
		generateLeaderboard("YEARLY", TimePeriodUtil.getCurrentYearBounds());
	}
	@Transactional
	@Override
	public void generateLeaderboardByType(String periodType){
		 Pair<LocalDate, LocalDate> bounds;
		    switch (periodType.toUpperCase()) {
		        case "WEEKLY":
		            bounds = TimePeriodUtil.getCurrentWeekBounds();
		            break;
		        case "MONTHLY":
		            bounds = TimePeriodUtil.getCurrentMonthBounds();
		            break;
		        case "QUARTERLY":
		            bounds = TimePeriodUtil.getCurrentQuarterBounds();
		            break;
		        case "YEARLY":
		            bounds = TimePeriodUtil.getCurrentYearBounds();
		            break;
		        default:
		            throw new IllegalArgumentException("Invalid period type: " + periodType);
		    }
		    generateLeaderboard(periodType,bounds);
	}

	@Transactional
	private void generateLeaderboard(String periodType, Pair<LocalDate, LocalDate> bounds) {
		LocalDate start = bounds.getLeft();
		LocalDate end = bounds.getRight();

		LOGGER.info("Generating {} leaderboard for period {} - {}", periodType, start, end);

		List<Long> userIds = userRepository.findAllUserIds();

		List<LeaderboardEntry> entries = userIds.stream().map(userId -> {
			int taskPoints = taskRepository.sumPointsByUserAndDateRange(userId, "APPROVED", start.atStartOfDay(),
					end.atTime(LocalTime.MAX));
			int recognitionPoints = recognitionRepository.sumRecognitionPointsByUserAndDateRange(userId,
					start.atStartOfDay(), end.atTime(LocalTime.MAX));
			return LeaderboardEntry.builder().userId(userId).totalTaskPoints(taskPoints)
					.totalRecognitionPoints(recognitionPoints).totalPoints(taskPoints + recognitionPoints)
					.periodStartDate(start).periodEndDate(end).periodType(periodType).createdAt(LocalDateTime.now())
					.build();
		}).collect(Collectors.toList());

		leaderboardRepository.saveAll(entries);

		// Send email to top 3
		List<LeaderboardEntry> top3 = entries.stream()
				.sorted(Comparator.comparingInt(LeaderboardEntry::getTotalPoints).reversed()).limit(3)
				.collect(Collectors.toList());

		emailTopPerformers(top3, periodType, start, end);
	}

	private void emailTopPerformers(List<LeaderboardEntry> top3, String periodType, LocalDate start, LocalDate end) {
		for (int i = 0; i < top3.size(); i++) {
			final int rank = i + 1; // Make it effectively final
			Long userId = top3.get(i).getUserId();

			userRepository.findById(userId).ifPresent(u -> {
				String subject = "🎉 You're a Top Performer on the " + periodType + " Leaderboard!";
				String message = String.format(
						"Congratulations %s!\nYou ranked #%d for %s (%s to %s).\nKeep up the great work!",
						u.getFirstName(), rank, periodType, start, end);

				try {
					emailService.sendEmail(List.of(u.getEmail()),Collections.emptyList(),subject,message);
				} catch (Exception e) {
					LOGGER.error("Failed to send email to {}: {}", u.getEmail(), e.getMessage());
				}
			});
		}
	}
	
	@Override
	public List<LeaderboardEntry> getLeaderboardByPeriod(String periodType) {
	    Pair<LocalDate, LocalDate> bounds;
	    switch (periodType.toUpperCase()) {
	        case "WEEKLY":
	            bounds = TimePeriodUtil.getCurrentWeekBounds();
	            break;
	        case "MONTHLY":
	            bounds = TimePeriodUtil.getCurrentMonthBounds();
	            break;
	        case "QUARTERLY":
	            bounds = TimePeriodUtil.getCurrentQuarterBounds();
	            break;
	        case "YEARLY":
	            bounds = TimePeriodUtil.getCurrentYearBounds();
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid period type: " + periodType);
	    }

	    LocalDate start = bounds.getLeft();
	    LocalDate end = bounds.getRight();

	    return leaderboardRepository.findByPeriodTypeAndPeriodStartDateAndPeriodEndDate(
	            periodType.toUpperCase(), start, end
	    );
	}


}
