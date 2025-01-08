package com.example.repository;

import com.example.entity.TokenStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TokenStatisticsRepository extends JpaRepository<TokenStatistics, Long> {

}
