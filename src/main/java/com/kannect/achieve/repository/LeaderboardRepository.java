package com.kannect.achieve.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kannect.achieve.entity.LeaderboardEntry;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {

	List<LeaderboardEntry> findByPeriodTypeAndPeriodStartDateAndPeriodEndDate(String upperCase, LocalDate start,
			LocalDate end);

}
