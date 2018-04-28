package com.molinaro.springbootionicbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molinaro.springbootionicbackend.domain.Cidade;
import com.molinaro.springbootionicbackend.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repo;
	
	public List<Cidade> findByEstado(Integer estadoId) {
		
		return repo.findCidades(estadoId);
	}
}
