package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domain.Student;
import ru.otus.services.LocaleService;

@Service
public class StudentServiceImpl implements StudentService {
    private final IOService ioService;
    private final LocaleService localeService;

    public StudentServiceImpl(IOService ioService, LocaleService localeService) {
        this.ioService = ioService;
        this.localeService = localeService;
    }

    @Override
    public Student getStudentInfo() {
        String firstName = ioService.readLineWithPrompt(localeService.getMessage("student.prompt.firstname"));
        String lastName = ioService.readLineWithPrompt(localeService.getMessage("student.prompt.lastname"));
        return new Student(firstName, lastName);
    }
}