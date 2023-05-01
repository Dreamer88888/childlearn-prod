package com.childlearn.service;

import com.childlearn.entity.Class;
import com.childlearn.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public List<Class> findAll() {
        return classRepository.findAll();
    }

    public Object findNameById(Long id) {
        return classRepository.findNameById(id);
    }

    public Class findById(Long id) {
        Optional<Class> cl = classRepository.findById(id);
        if (cl.isPresent()) {
            return cl.get();
        } else {
            return null;
        }
    }

    public Class createClass(Class cl) {
        return classRepository.save(cl);
    }

    public Class updateClass(Class cl) {
        Long id = cl.getId();
        Optional<Class> classFromDB = classRepository.findById(id);
        if (!classFromDB.isEmpty()) {
            return classRepository.save(cl);
        } else {
            return null;
        }
    }

    public boolean deleteClass(Long id) {
        Optional<Class> classFromDB = classRepository.findById(id);
        if (!classFromDB.isEmpty()) {
            classRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}