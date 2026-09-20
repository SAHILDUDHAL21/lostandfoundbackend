package com.sahil.lostandfound.controller;

import com.sahil.lostandfound.entity.Claim;
import com.sahil.lostandfound.entity.ScammerProfile;
import com.sahil.lostandfound.repository.ClaimRepository;
import com.sahil.lostandfound.repository.ScammerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ScammerRepository scammerRepository;

    @GetMapping("/item/{itemId}")
    public List<Claim> getClaimsByItem(@PathVariable Long itemId) {
        return claimRepository.findByItemIdOrderByIdDesc(itemId);
    }

    @PostMapping
    public ResponseEntity<Claim> submitClaim(@RequestBody Claim claim) {
        if (claim.getStatus() == null || claim.getStatus().isBlank()) {
            claim.setStatus("PENDING_VERIFICATION");
        }
        if (claim.getTime() == null || claim.getTime().isBlank()) {
            claim.setTime("Just now");
        }
        Claim savedClaim = claimRepository.save(claim);
        return ResponseEntity.ok(savedClaim);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Claim> updateClaimStatus(@PathVariable Long id, @RequestParam String status) {
        return claimRepository.findById(id).map(existing -> {
            existing.setStatus(status);
            Claim saved = claimRepository.save(existing);

            // Automatically flag rejected claim user in Scammer Leaderboard
            if ("REJECTED".equalsIgnoreCase(status) && existing.getUser() != null && !existing.getUser().isBlank()) {
                String userName = existing.getUser();
                String handle = "@" + userName.toLowerCase().replaceAll("\\s+", "_");
                String avatar = (existing.getAvatar() != null && !existing.getAvatar().isBlank()) 
                    ? existing.getAvatar() 
                    : "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><circle cx='50' cy='50' r='50' fill='%2330281e'/><circle cx='50' cy='40' r='20' fill='%23faf5e9'/><path d='M20,85 C20,65 35,60 50,60 C65,60 80,65 80,85 Z' fill='%23faf5e9'/></svg>";

                scammerRepository.findByFullNameIgnoreCase(userName).ifPresentOrElse(scammer -> {
                    scammer.setFailedClaimsCount(scammer.getFailedClaimsCount() + 1);
                    scammer.setReason("Rejected ownership claim for item #" + existing.getItemId());
                    scammer.setStatus("Flagged Scammer");
                    if (scammer.getAvatar() == null || scammer.getAvatar().isBlank()) {
                        scammer.setAvatar(avatar);
                    }
                    scammerRepository.save(scammer);
                }, () -> {
                    ScammerProfile newScammer = new ScammerProfile(
                        userName,
                        handle,
                        avatar,
                        1,
                        "Rejected ownership claim for item #" + existing.getItemId(),
                        "Flagged Scammer"
                    );
                    scammerRepository.save(newScammer);
                });
            }

            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        if (claimRepository.existsById(id)) {
            claimRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
