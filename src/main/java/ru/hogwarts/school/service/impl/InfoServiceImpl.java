package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.service.InfoService;

import java.util.stream.IntStream;
@Service
public class InfoServiceImpl implements InfoService {

    @Value("${server.port}")
    private int serverPort;
    @Override
    public int getInt(int number) {
        return IntStream.rangeClosed(1, number)
                .parallel()
                .sum();
    }

    @Override
    public int getPort() {
        return serverPort;
    }
}
