package com.example.smartngobackend.controller;

import com.example.smartngobackend.entity.Campaign;
import com.example.smartngobackend.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin(origins = "*")
public class CampaignController {

    @Autowired
    private CampaignRepository campaignRepository;

    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        List<Campaign> campaigns = campaignRepository.findAll();

        // Seed default campaigns if table is empty
        if (campaigns.isEmpty()) {
            campaignRepository.save(new Campaign(
                    "Feed a Family",
                    "FOOD & HUNGER",
                    "Help provide nutritious meals to families in need.",
                    new BigDecimal("100000.00"),
                    new BigDecimal("68000.00")
            ));
            campaignRepository.save(new Campaign(
                    "Education for Every Child",
                    "EDUCATION",
                    "Support books, school supplies and learning resources.",
                    new BigDecimal("150000.00"),
                    new BigDecimal("82500.00")
            ));
            campaignRepository.save(new Campaign(
                    "Healthcare for All",
                    "HEALTHCARE",
                    "Help communities access essential medical support.",
                    new BigDecimal("200000.00"),
                    new BigDecimal("90000.00")
            ));
            campaignRepository.save(new Campaign(
                    "Care for Street Animals",
                    "ANIMAL WELFARE",
                    "Support food and care for vulnerable street animals.",
                    new BigDecimal("75000.00"),
                    new BigDecimal("45000.00")
            ));
            campaigns = campaignRepository.findAll();
        }

        return ResponseEntity.ok(campaigns);
    }

    @PostMapping
    public ResponseEntity<?> createCampaign(@RequestBody Campaign campaign) {
        if (campaign.getTitle() == null || campaign.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Campaign title is required.");
        }
        Campaign savedCampaign = campaignRepository.save(campaign);
        return new ResponseEntity<>(savedCampaign, HttpStatus.CREATED);
    }
}
