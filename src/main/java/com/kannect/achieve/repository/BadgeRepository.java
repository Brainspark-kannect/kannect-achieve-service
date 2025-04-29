package com.kannect.achieve.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kannect.achieve.entity.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long>{

	Optional<Badge> findByBadgeType(String type);

}
