package com.childlearn.service;

import com.childlearn.dto.TeacherDto;
import com.childlearn.dto.TeacherUpdateDto;
import com.childlearn.entity.Position;
import com.childlearn.entity.Teacher;
import com.childlearn.entity.User;
import com.childlearn.repository.PositionRepository;
import com.childlearn.repository.TeacherRepository;
import com.childlearn.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionService positionService;

    private static final String WAKIL_KEPALA_SEKOLAH = "Wakil Kepala Sekolah";
    private static final String WALI_KELAS = "Wali Kelas";

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public List<TeacherDto> findAllTeacherDto() {
        List<TeacherDto> teacherDtos = findAll().stream().map(teacher -> {
            return findById(teacher.getId());
        }).collect(Collectors.toList());

        return teacherDtos;
    }

    public TeacherDto findByUserId(Long userId) {
        Optional<Teacher> teacher = teacherRepository.findByUserId(userId);

        if (teacher.isPresent()) {
            return findById(teacher.get().getId());
        } else {
            throw new NotFoundException("Teacher with User Id " + userId.toString() + " not found.");
        }
    }

//    public TeacherDto findHeadMaster() {
//        Position position = positionService.findByName(KEPALA_SEKOLAH);
//        if (position != null) {
//            List<TeacherDto> teachers = findByPositionId(position.getId());
//            return teachers.get(0);
//        } else {
//            throw new NotFoundException(KEPALA_SEKOLAH + " not found.");
//        }
//    }

    public TeacherDto findViceHeadMaster() {
        Position position = positionService.findByName(WAKIL_KEPALA_SEKOLAH);
        if (position != null) {
            List<TeacherDto> teachers = findByPositionId(position.getId());
            return teachers.get(0);
        } else {
            throw new NotFoundException(WAKIL_KEPALA_SEKOLAH + " not found.");
        }
    }

    public List<TeacherDto> findHomeRoomTeacher() {
        Position position = positionService.findByName(WALI_KELAS);
        if (position != null) {
            List<TeacherDto> teachers = findByPositionId(position.getId());
            return teachers;
        } else {
            throw new NotFoundException(WALI_KELAS + "+ not found.");
        }
    }

    public List<TeacherDto> findGeneralTeacher() {
        List<Long> positionIds = new ArrayList<>();

//        positionIds.add(positionService.findByName(WAKIL_KEPALA_SEKOLAH).getId());
        positionIds.add(positionService.findByName(WALI_KELAS).getId());

        Optional<List<Teacher>> teacherDtosFromDB = teacherRepository.findByPositionIdNotIn(positionIds);
        if (!teacherDtosFromDB.get().isEmpty()) {
            List<TeacherDto> teacherDtos = teacherDtosFromDB.get().stream().map(teacher -> {
                Optional<User> user = userRepository.findById(teacher.getUserId());
                Optional<Position> position = positionRepository.findById(teacher.getPositionId());
                TeacherDto teacherDto = new TeacherDto();

                if (user.isPresent() && position.isPresent()) {
                    teacherDto = new TeacherDto(teacher.getId(), user.get(), position.get());
                }

                return teacherDto;
            }).collect(Collectors.toList());

            return teacherDtos;
        } else {
            throw new NotFoundException("General Teacher not found");
        }
    }

    private List<TeacherDto> findByPositionId(Long positionId) {
        Optional<List<Teacher>> teachersFromDB = teacherRepository.findByPositionId(positionId);
        if (!teachersFromDB.get().isEmpty()) {
            List<TeacherDto> teacherDtos = teachersFromDB.get().stream().map(teacher -> {
                Optional<User> user = userRepository.findById(teacher.getUserId());
                Optional<Position> position = positionRepository.findById(teacher.getPositionId());
                TeacherDto teacherDto = new TeacherDto();

                if (user.isPresent() && position.isPresent()) {
                    teacherDto = new TeacherDto(teacher.getId(), user.get(), position.get());
                }

                return teacherDto;
            }).collect(Collectors.toList());

            return teacherDtos;
        } else {
            throw new NotFoundException("Teacher with position Id" + positionId.toString() + " not found.");
        }
    }

    public TeacherDto findById(Long id) {
        Optional<Teacher> teacherFromDB = teacherRepository.findById(id);
        Optional<User> user = userRepository.findById(teacherFromDB.get().getUserId());
        Optional<Position> position = positionRepository.findById(teacherFromDB.get().getPositionId());
        if (!teacherFromDB.isEmpty()) {
            TeacherDto teacher = new TeacherDto();
            teacher.setId(id);
            teacher.setUser(user.get());
            teacher.setPosition(position.get());

            return teacher;
        } else {
            return null;
        }
    }

    public Teacher createTeacher(Teacher teacher) {
        User user = userService.findById(teacher.getUserId());
        if (user != null) {
            user.setRole("TEACHER");
            userService.updateUser(user);
        }
        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher updateTeacher(TeacherUpdateDto teacherUpdateDto) {
        Long id = teacherUpdateDto.getId();
        Long userId = teacherUpdateDto.getUserId();
        Long positionId = teacherUpdateDto.getPositionId();

        Optional<Teacher> updatedTeacher = teacherRepository.findById(id);
        Optional<User> updatedUser = userRepository.findById(userId);
        Optional<Position> updatedPosition = positionRepository.findById(positionId);

        if (!updatedTeacher.isEmpty() && !updatedPosition.isEmpty()) {
            Teacher teacher = new Teacher();
            teacher.setId(id);
            teacher.setUserId(userId);
            teacher.setPositionId(positionId);
            teacher = teacherRepository.save(teacher);

            User user = updatedUser.get();
            user.setFullName(teacherUpdateDto.getFullName());
            userService.updateUser(user);

            return teacher;
        } else {
            return null;
        }
    }

    public boolean deleteTeacher(Long id) {
        Optional<Teacher> deletedTeacher = teacherRepository.findById(id);
        if (!deletedTeacher.isEmpty()) {
            teacherRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
