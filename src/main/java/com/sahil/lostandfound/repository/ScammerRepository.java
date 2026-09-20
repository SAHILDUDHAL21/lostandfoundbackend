package com.sahil.lostandfound.repository;

import com.sahil.lostandfound.entity.ScammerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ScammerRepository extends JpaRepository<ScammerProfile, Long> {
    List<ScammerProfile> findAllByOrderByFailedClaimsCountDesc();
    List<ScammerProfile> findTop100ByOrderByFailedClaimsCountDesc();
    Optional<ScammerProfile> findByFullNameIgnoreCase(String fullName);
}
