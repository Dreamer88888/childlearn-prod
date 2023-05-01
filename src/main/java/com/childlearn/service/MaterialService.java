package com.childlearn.service;

import com.childlearn.dto.MaterialDto;
import com.childlearn.entity.Class;
import com.childlearn.entity.Material;
import com.childlearn.entity.Subject;
import com.childlearn.repository.ClassRepository;
import com.childlearn.repository.MaterialRepository;
import com.childlearn.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    public MaterialDto findMaterialById(Long id) {
        Material material = materialRepository.findById(id).get();
        Optional<Class> cl = classRepository.findById(material.getClassId());
        Optional<Subject> subject = subjectRepository.findById(material.getSubjectId());

        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(id);
        materialDto.setCl(cl.get());
        materialDto.setSubject(subject.get());
        materialDto.setTitle(material.getTitle());
        materialDto.setFile(material.getFile());

        return materialDto;
    }

    public List<Material> findMaterialByClassIdAndSubjectId(Long classId, Long subjectId) {
        List<Material> materials = materialRepository.findByClassIdAndSubjectId(classId, subjectId);

        return materials;
    }

    public List<Material> findBySubjectId(Long id) {
        return materialRepository.findBySubjectId(id);
    }

    public Material createMaterial(MaterialDto materialDto) throws IOException {

        Material material = new Material();
        material.setTitle(materialDto.getTitle());
        material.setClassId(materialDto.getCl().getId());
        material.setSubjectId(materialDto.getSubject().getId());
        material.setFile(materialDto.getFile());

        return materialRepository.save(material);
    }

    public Material updateMaterial(Material material) {
        Long id = material.getId();
        Optional<Material> materialFromDB = materialRepository.findById(id);
        if (!materialFromDB.isEmpty()) {
            return materialRepository.save(material);
        } else {
            return null;
        }
    }

    public boolean deleteMaterial(Long id) {
        Optional<Material> materialFromDB = materialRepository.findById(id);
        if (!materialFromDB.isEmpty()) {
            materialRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
