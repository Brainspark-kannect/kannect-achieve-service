package com.kannect.achieve.service;

import java.util.List;

import com.kannect.achieve.entity.LeaderboardEntry;

public interface LeaderboardService {

	void generateYearlyLeaderboard();

	void generateQuarterlyLeaderboard();

	void generateMonthlyLeaderboard();

	void generateWeeklyLeaderboard();

	void generateLeaderboardByType(String periodType);

	List<LeaderboardEntry> getLeaderboardByPeriod(String periodType);

}
