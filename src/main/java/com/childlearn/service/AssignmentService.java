package com.childlearn.service;

import com.childlearn.dto.AssignmentDto;
import com.childlearn.dto.StudentAsgDto;
import com.childlearn.dto.StudentDto;
import com.childlearn.entity.AnswerSheet;
import com.childlearn.entity.Assignment;
import com.childlearn.entity.Student;
import com.childlearn.repository.AssignmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AnswerSheetService answerSheetService;

    @Autowired
    private StudentService studentService;

    public List<Assignment> findAll() {
        return assignmentRepository.findAll();
    }

    public Assignment findById(Long id) {
        Optional<Assignment> assignment = assignmentRepository.findById(id);
        if (assignment.isPresent()) {
            return assignment.get();
        } else {
            return null;
        }
    }

    public List<Assignment> findBySubjectId(Long subjectId) {
        return assignmentRepository.findBySubjectId(subjectId);
    }

    public List<Assignment> findBySubjectIdAndClassId(Long subjectId, Long classId) {
        return assignmentRepository.findBySubjectIdAndClassId(subjectId, classId);
    }

    public List<StudentAsgDto> findStudentAsgDtoBySubjectIdAndClassIdAndStudentId(Long subjectId, Long classId, Long studentId) {
        List<StudentAsgDto> studentAsgDtos = findBySubjectIdAndClassId(subjectId, classId).stream().map(assignment -> {

            StudentAsgDto studentAsgDto = new StudentAsgDto();
            studentAsgDto.setId(assignment.getId());
            studentAsgDto.setTitle(assignment.getTitle());
            studentAsgDto.setDeadline(assignment.getDeadline());
            studentAsgDto.setSubjectId(subjectId);

            AnswerSheet answerSheetFromDB = answerSheetService.findByAssignmentIdAndStudentId(assignment.getId(), studentId);

            if (answerSheetFromDB != null) {
                studentAsgDto.setAnswerSheetId(answerSheetFromDB.getId());
                if (answerSheetFromDB.getScore() >= 1) {
                    studentAsgDto.setStatus("Selesai");
                    studentAsgDto.setScore(answerSheetFromDB.getScore());
                } else {
                    studentAsgDto.setStatus("Belum Dinilai");
                    studentAsgDto.setScore(-1);
                }
            } else {
                studentAsgDto.setAnswerSheetId(null);
                studentAsgDto.setStatus("Belum Dikerjakan");
                studentAsgDto.setScore(-1);
            }

            return studentAsgDto;
        }).collect(Collectors.toList());

        return studentAsgDtos;
    }

    public List<AssignmentDto> findAssignmentDtoBySubjectIdAndClassId(Long subjectId, Long classId) {
        List<AssignmentDto> assignmentDtos = findBySubjectIdAndClassId(subjectId, classId).stream().map(assignment -> {

            AssignmentDto assignmentDto = new AssignmentDto();
            assignmentDto.setId(assignment.getId());
            assignmentDto.setTitle(assignment.getTitle());
            assignmentDto.setDeadline(assignment.getDeadline());
            assignmentDto.setSubjectId(subjectId);

            List<AnswerSheet> answerSheets = answerSheetService.findByAssignmentId(assignment.getId());
            List<StudentDto> studentDtos = studentService.findAllStudentDtoByClassId(classId);

            List<String> studentNames = new ArrayList<>();
            List<Long> studentIds = new ArrayList<>();
            List<Long> answerSheetIds = new ArrayList<>();
            List<Integer> scores = new ArrayList<>();
            List<String> statuses = new ArrayList<>();

            for (StudentDto studentDto : studentDtos) {
                log.info(studentDto.getUser().getFullName());
                boolean isAnswerSheetNotUploadedYet = true;
                studentNames.add(studentDto.getUser().getFullName());
                studentIds.add(studentDto.getId());
                for (AnswerSheet answerSheet : answerSheets) {
                    if (answerSheet.getStudentId() == studentDto.getId()) {
                        Long answerSheetId = answerSheet.getId();
                        answerSheetIds.add(answerSheetId);

                        if (answerSheet.getScore() >= 0) {
                            statuses.add("Selesai");
                            scores.add(answerSheet.getScore());
                        } else {
                            statuses.add("Belum Dinilai");
                            scores.add(-1);
                        }

                        isAnswerSheetNotUploadedYet = false;
                    }
                }
                log.info("masuk sni");
                if (isAnswerSheetNotUploadedYet) {
                    answerSheetIds.add(null);
                    statuses.add("Belum Dikerjakan");
                    scores.add(-1);
                }
            }
            assignmentDto.setStudentName(studentNames);
            assignmentDto.setStudentId(studentIds);
            assignmentDto.setAnswerSheetId(answerSheetIds);
            assignmentDto.setScore(scores);
            assignmentDto.setStatus(statuses);

            return assignmentDto;
        }).collect(Collectors.toList());

        return assignmentDtos;
    }

//    public List<AssignmentDto> findAssignmentDtoBySubjectId(Long subjectId) {
//        List<AssignmentDto> assignmentDtos = findBySubjectId(subjectId).stream().map(assignment -> {
//            AnswerSheet answerSheet = answerSheetService.findById(assignment.getAnswerSheetId());
//
//            AssignmentDto assignmentDto = new AssignmentDto();
//            assignmentDto.setId(assignment.getId());
//            assignmentDto.setTitle(assignment.getTitle());
//            assignmentDto.setDeadline(assignment.getDeadline());
//            assignmentDto.setSubjectId(subjectId);
//
//            Long answerSheetId = assignment.getAnswerSheetId();
//            if (!answerSheetId.equals(0L) || answerSheet != null) {
//
//                assignmentDto.setAnswerSheetId(assignment.getAnswerSheetId());
//
//                if (answerSheet.getScore() >= 0) {
//                    assignmentDto.setStatus("Selesai");
//                    assignmentDto.setScore(answerSheet.getScore());
//                } else {
//                    assignmentDto.setStatus("Belum Dinilai");
//                    assignmentDto.setScore(-1);
//                }
//            } else {
//                assignmentDto.setAnswerSheetId(answerSheetId);
//                assignmentDto.setStatus("Belum Dikerjakan");
//                assignmentDto.setScore(-1);
//            }
//
//            return assignmentDto;
//        }).collect(Collectors.toList());
//
//        return assignmentDtos;
//    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Assignment assignment) {
        Long id = assignment.getId();
        Optional<Assignment> updatedAssignment = assignmentRepository.findById(id);
        if (updatedAssignment.isPresent()) {
            return assignmentRepository.save(assignment);
        } else {
            return null;
        }
    }

    public boolean deleteAssignment(Long id) {
        Optional<Assignment> deletedAssignment = assignmentRepository.findById(id);
        if (deletedAssignment.isPresent()) {
            assignmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
