package com.childlearn.service;

import com.childlearn.entity.AnswerSheet;
import com.childlearn.repository.AnswerSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerSheetService {

    @Autowired
    private AnswerSheetRepository answerSheetRepository;

    public List<AnswerSheet> findAll() {
        return answerSheetRepository.findAll();
    }

    public AnswerSheet findById(Long id) {
        Optional<AnswerSheet> answerSheet = answerSheetRepository.findById(id);
        if (answerSheet.isPresent()) {
            return answerSheet.get();
        } else {
            return null;
        }
    }

    public List<AnswerSheet> findByAssignmentId(Long assignmentId) {
        Optional<List<AnswerSheet>> answerSheets = answerSheetRepository.findByAssignmentId(assignmentId);
        if (answerSheets.isPresent()) {
            return answerSheets.get();
        } else {
            return null;
        }
    }

    public AnswerSheet findByAssignmentIdAndStudentId(Long assignmentId, Long studentId) {
        Optional<AnswerSheet> answerSheet = answerSheetRepository.findByAssignmentIdAndStudentId(assignmentId, studentId);
        if (answerSheet.isPresent()) {
            return answerSheet.get();
        } else {
            return null;
        }
    }

    public AnswerSheet createAnswerSheet(AnswerSheet answerSheet) {
        return answerSheetRepository.save(answerSheet);
    }

    public AnswerSheet updateAnswerSheet(AnswerSheet answerSheet) {
        Long id = answerSheet.getId();
        Optional<AnswerSheet> updatedAnswerSheet = answerSheetRepository.findById(id);
        if (updatedAnswerSheet.isPresent()) {
            return answerSheetRepository.save(answerSheet);
        } else {
            return null;
        }
    }

    public boolean deleteAnswerSheet(Long id) {
        Optional<AnswerSheet> deletedAnswerSheet = answerSheetRepository.findById(id);
        if (deletedAnswerSheet.isPresent()) {
            answerSheetRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
