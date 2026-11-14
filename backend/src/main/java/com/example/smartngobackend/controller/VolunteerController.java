package com.example.smartngobackend.controller;

import com.example.smartngobackend.entity.Volunteer;
import com.example.smartngobackend.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
@CrossOrigin(origins = "*")
public class VolunteerController {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @PostMapping
    public ResponseEntity<?> registerVolunteer(@RequestBody Volunteer volunteer) {
        if (volunteer.getFullName() == null || volunteer.getFullName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Full name is required.");
        }
        if (volunteer.getPhoneNumber() == null || volunteer.getPhoneNumber().trim().length() != 10) {
            return ResponseEntity.badRequest().body("Valid 10-digit phone number is required.");
        }
        if (volunteer.getEmail() == null || !volunteer.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body("Valid email address is required.");
        }

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);
        return new ResponseEntity<>(savedVolunteer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVolunteerById(@PathVariable Long id) {
        return volunteerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
