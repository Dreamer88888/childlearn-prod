package com.childlearn.service;

import com.childlearn.dto.StudentDto;
import com.childlearn.dto.StudentUpdateDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.Student;
import com.childlearn.entity.User;
import com.childlearn.repository.ClassRepository;
import com.childlearn.repository.StudentRepository;
import com.childlearn.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<StudentDto> findAllStudentDtoByClassId(Long classId) {
        List<StudentDto> studentDtos = studentRepository.findByClassId(classId).stream().map(student -> {
            return findById(student.getId());
        }).collect(Collectors.toList());

        return studentDtos;
    }

    public StudentDto findById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (!student.isEmpty()) {
            Long classId = student.get().getClassId();
            Long userId = student.get().getUserId();

            Optional<Class> cl = classRepository.findById(classId);
            Optional<User> user = userRepository.findById(userId);

            StudentDto studentDto = new StudentDto();
            studentDto.setId(id);
            studentDto.setCl(cl.get());
            studentDto.setUser(user.get());

            return studentDto;
        } else {
            return null;
        }
    }

    public List<Student> findByClassId(Long classId) {
        return studentRepository.findByClassId(classId);
    }

    public Student findByUserId(Long userId) {
        Optional<Student> studentFromDB = studentRepository.findByUserId(userId);
        if (studentFromDB.isPresent()) {
            return studentFromDB.get();
        } else {
            throw new NotFoundException("Student with User Id " + userId.toString() + " not found.");
        }
    }

    public Student createStudent(Student student) {
        User user = userService.findById(student.getUserId());
        if (user != null) {
            user.setRole("STUDENT");
            userService.updateUser(user);
        }
        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(StudentUpdateDto studentUpdateDto) {
        Long id = studentUpdateDto.getId();
        Long userId = studentUpdateDto.getUserId();

        Optional<User> userFromDB = userRepository.findById(userId);

        if (!studentRepository.findById(id).isEmpty() && !userFromDB.isEmpty()) {
            Student student = new Student(id, studentUpdateDto.getClassId(), userId);
            userFromDB.get().setFullName(studentUpdateDto.getFullName());
            userRepository.save(userFromDB.get());
            return studentRepository.save(student);
        } else {
            return null;
        }
    }

    public boolean deleteStudent(Long id) {
        if (!studentRepository.findById(id).isEmpty()) {
            studentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
