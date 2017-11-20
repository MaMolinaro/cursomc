package com.molinaro.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molinaro.cursomc.domain.Categoria;
import com.molinaro.cursomc.repositories.CategoriaRepository;
import com.molinaro.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		
		Categoria obj = repo.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFoundException("Não existe Catedoria com id: #" + id);
		}
		
		return obj;
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
}
