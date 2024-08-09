package com.cafehub.backend.cafe.repository;

import com.cafehub.backend.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {
}
