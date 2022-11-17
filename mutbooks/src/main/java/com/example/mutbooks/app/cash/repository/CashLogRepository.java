package com.example.mutbooks.app.cash.repository;

import com.example.mutbooks.app.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
