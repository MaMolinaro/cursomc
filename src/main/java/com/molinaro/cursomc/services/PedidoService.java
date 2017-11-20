package com.molinaro.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molinaro.cursomc.domain.Pedido;
import com.molinaro.cursomc.repositories.PedidoRepository;
import com.molinaro.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	public Pedido find(Integer id) {
		
		Pedido obj = repo.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFoundException("Não existe Catedoria com id: #" + id);
		}
		
		return obj;
	}
}
