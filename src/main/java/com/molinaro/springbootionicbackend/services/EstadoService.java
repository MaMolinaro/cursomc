package com.molinaro.springbootionicbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molinaro.springbootionicbackend.domain.Estado;
import com.molinaro.springbootionicbackend.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repo;
	
	public List<Estado> findAll() {
		
		return repo.findByOrderByNome();
	}
}
