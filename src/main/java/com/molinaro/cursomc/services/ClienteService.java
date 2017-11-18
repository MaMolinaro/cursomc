package com.molinaro.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molinaro.cursomc.domain.Cliente;
import com.molinaro.cursomc.repositories.ClienteRepository;
import com.molinaro.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id) {
		
		Cliente obj = repo.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFoundException("Não existe Cliente com id: #" + id);
		}
		
		return obj;
	}
}
