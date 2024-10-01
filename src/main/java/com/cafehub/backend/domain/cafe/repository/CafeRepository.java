package com.cafehub.backend.domain.cafe.repository;

import com.cafehub.backend.domain.cafe.entity.Cafe;
import com.cafehub.backend.domain.cafe.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long>,CafeRepositoryCustom {


}