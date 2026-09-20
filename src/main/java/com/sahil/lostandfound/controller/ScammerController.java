package com.sahil.lostandfound.controller;

import com.sahil.lostandfound.entity.ScammerProfile;
import com.sahil.lostandfound.repository.ScammerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scammers")
@CrossOrigin(origins = "*")
public class ScammerController {

    @Autowired
    private ScammerRepository scammerRepository;

    @GetMapping
    public List<ScammerProfile> getScammerLeaderboard() {
        return scammerRepository.findTop100ByOrderByFailedClaimsCountDesc();
    }

    @PostMapping
    public ResponseEntity<ScammerProfile> addScammerProfile(@RequestBody ScammerProfile scammer) {
        if (scammer.getStatus() == null || scammer.getStatus().isBlank()) {
            scammer.setStatus("Flagged Scammer");
        }
        ScammerProfile saved = scammerRepository.save(scammer);
        return ResponseEntity.ok(saved);
    }
}
