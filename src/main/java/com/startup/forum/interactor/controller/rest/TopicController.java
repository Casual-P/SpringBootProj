package com.startup.forum.interactor.controller.rest;

import com.startup.forum.domain.model.rest.TopicDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/topic")
public class TopicController {

    @GetMapping("/all")
    public List<TopicDto> getAll(@RequestParam long page, @RequestParam(defaultValue = "10") long size) {
        return List.of();
    }

    @GetMapping("/{id}")
    public TopicDto getById(@PathVariable Long id) {
        return null;
    }
}
