package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class InfoController {
    private final InfoService infoService;

    @GetMapping("/getPort")
    public int getPort() {
        return infoService.getPort();
    }
    @GetMapping("/sum/{number}")
    public int getInt(@PathVariable int number){
        return infoService.getInt(number);
    }

}

