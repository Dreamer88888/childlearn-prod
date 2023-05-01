package com.childlearn.service;

import com.childlearn.dto.AgendaDetailRequestDto;
import com.childlearn.entity.AgendaDetail;
import com.childlearn.repository.AgendaDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgendaDetailService {

    @Autowired
    private AgendaDetailRepository agendaDetailRepository;

    public List<AgendaDetail> findAll() {
        return agendaDetailRepository.findAll();
    }

    public List<AgendaDetail> findByAgendaHeaderId(Long agendaHeaderId) {
        return agendaDetailRepository.findByAgendaHeaderId(agendaHeaderId);
    }

    public AgendaDetail findById(Long id) {
        Optional<AgendaDetail> agendaDetail = agendaDetailRepository.findById(id);
        if (agendaDetail.isPresent()) {
            return agendaDetail.get();
        } else {
            return null;
        }
    }

    public AgendaDetail createAgendaDetail(AgendaDetailRequestDto agendaDetailRequestDto) {
        AgendaDetail agendaDetail = new AgendaDetail();
        agendaDetail.setAgendaHeaderId(agendaDetailRequestDto.getAgendaHeaderId());
        agendaDetail.setSubjectId(agendaDetailRequestDto.getSubjectId());
        agendaDetail.setTitle(agendaDetailRequestDto.getTitle());

        return agendaDetailRepository.save(agendaDetail);
    }

    public AgendaDetail updateAgendaDetail(AgendaDetail agendaDetail) {
        Long agendaDetailId = agendaDetail.getId();
        Optional<AgendaDetail> updatedAgendaDetail = agendaDetailRepository.findById(agendaDetailId);
        if (updatedAgendaDetail.isPresent()) {
            return agendaDetailRepository.save(agendaDetail);
        } else {
            return null;
        }
    }

    public boolean deleteAgendaDetail(Long id) {
        Optional<AgendaDetail> deletedAgendaDetail = agendaDetailRepository.findById(id);
        if (deletedAgendaDetail.isPresent()) {
            agendaDetailRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
