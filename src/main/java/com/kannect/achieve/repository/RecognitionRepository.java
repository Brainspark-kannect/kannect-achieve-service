package com.kannect.achieve.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kannect.achieve.entity.Recognition;

@Repository
public interface RecognitionRepository extends JpaRepository<Recognition, Long> {
	@Query("SELECT r FROM Recognition r WHERE r.receiverId = :userId AND r.dateGiven BETWEEN :start AND :end")
	List<Recognition> findReceivedByUserWithinPeriod(@Param("userId") Long userId, @Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	@Query("SELECT r FROM Recognition r WHERE r.giverId = :userId AND r.dateGiven BETWEEN :start AND :end")
	List<Recognition> findGivenByUserWithinPeriod(@Param("userId") Long userId, @Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	@Query("SELECT r FROM Recognition r WHERE r.approved = true AND r.dateGiven BETWEEN :start AND :end")
	List<Recognition> findApprovedWithinPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	
	int countByGiverIdAndDateGivenAfter(Long giverId, LocalDateTime date);

	boolean existsByReceiverIdAndBadgeIdAndDateGivenAfter(Long receiverId, Long badgeId, LocalDateTime date);
	
	@Query("SELECT COALESCE(SUM(r.badge.recognitionPoints), 0) FROM Recognition r WHERE r.receiverId = :userId AND r.dateGiven BETWEEN :start AND :end AND r.approved = true")
	int sumRecognitionPointsByUserAndDateRange(@Param("userId") Long userId,
	                                           @Param("start") LocalDateTime start,
	                                           @Param("end") LocalDateTime end);


}
