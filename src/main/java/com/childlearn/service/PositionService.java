package com.childlearn.service;

import com.childlearn.entity.Position;
import com.childlearn.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    public Position findById(Long id) {
        Optional<Position> position = positionRepository.findById(id);
        if (position.isPresent()) {
            return position.get();
        } else {
            return null;
        }
    }

    public Position findByName(String name) {
        Optional<Position> position = positionRepository.findByName(name);
        if (position.isPresent()) {
            return position.get();
        } else {
            return null;
        }
    }

    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    public Position updatePosition(Position position) {
        Long id = position.getId();
        Optional<Position> positionFromDB = positionRepository.findById(id);
        if (!positionFromDB.isEmpty()) {
            return positionRepository.save(position);
        } else {
            return null;
        }
    }

    public boolean deletePosition(Long id) {
        Optional<Position> positionFromDB = positionRepository.findById(id);
        if (!positionFromDB.isEmpty()) {
            positionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
