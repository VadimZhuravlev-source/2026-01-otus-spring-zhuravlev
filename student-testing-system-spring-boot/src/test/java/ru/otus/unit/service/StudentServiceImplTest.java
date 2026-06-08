package ru.otus.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.Student;
import ru.otus.services.IOService;
import ru.otus.services.LocaleService;
import ru.otus.services.StudentServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private LocaleService localeService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        // Mock LocaleService to return English prompts
        when(localeService.getMessage("student.prompt.firstname")).thenReturn("Please enter your first name: ");
        when(localeService.getMessage("student.prompt.lastname")).thenReturn("Please enter your last name: ");
    }

    @Test
    void getStudentInfo_shouldReturnStudentWithCorrectNames() {
        when(ioService.readLineWithPrompt("Please enter your first name: ")).thenReturn("John");
        when(ioService.readLineWithPrompt("Please enter your last name: ")).thenReturn("Doe");

        Student student = studentService.getStudentInfo();

        assertNotNull(student);
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        verify(ioService, times(1)).readLineWithPrompt("Please enter your first name: ");
        verify(ioService, times(1)).readLineWithPrompt("Please enter your last name: ");
    }

    @Test
    void getStudentInfo_shouldHandleEmptyNames() {
        when(ioService.readLineWithPrompt("Please enter your first name: ")).thenReturn("");
        when(ioService.readLineWithPrompt("Please enter your last name: ")).thenReturn("");

        Student student = studentService.getStudentInfo();

        assertNotNull(student);
        assertEquals("", student.getFirstName());
        assertEquals("", student.getLastName());
    }

    @Test
    void getStudentInfo_shouldHandleNamesWithSpaces() {
        when(ioService.readLineWithPrompt("Please enter your first name: ")).thenReturn("Mary Jane");
        when(ioService.readLineWithPrompt("Please enter your last name: ")).thenReturn("Smith Watson");

        Student student = studentService.getStudentInfo();

        assertNotNull(student);
        assertEquals("Mary Jane", student.getFirstName());
        assertEquals("Smith Watson", student.getLastName());
    }
}