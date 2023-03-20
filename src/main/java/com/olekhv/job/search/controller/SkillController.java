package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.skill.Skill;
import com.olekhv.job.search.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/skills")
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/add")
    public ResponseEntity<List<Skill>> addSkillsToUser(@RequestParam("id") List<Long> skillsId,
                                                       @AuthenticationPrincipal UserCredential userCredential) {
        return ResponseEntity.ok(skillService.addSkillsToUser(skillsId, userCredential));
    }
}
