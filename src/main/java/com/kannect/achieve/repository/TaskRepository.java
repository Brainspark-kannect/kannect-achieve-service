package com.kannect.achieve.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kannect.achieve.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByAssignedTo(Long userId);

	List<Task> findByStatusIn(List<String> status);

	@Query("SELECT COALESCE(SUM(10), 0) FROM Task t WHERE t.assignedTo = :userId AND t.status = :status AND t.completedAt BETWEEN :start AND :end")
	int sumPointsByUserAndDateRange(@Param("userId") Long userId, @Param("status") String status,
			@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
