package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domain.Student;

@Service
public class StudentServiceImpl implements StudentService {
    private final IOService ioService;

    public StudentServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Student getStudentInfo() {
        String firstName = ioService.readLineWithPrompt("Please enter your first name: ");
        String lastName = ioService.readLineWithPrompt("Please enter your last name: ");
        return new Student(firstName, lastName);
    }
}