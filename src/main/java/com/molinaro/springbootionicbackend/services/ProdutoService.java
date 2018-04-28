package com.molinaro.springbootionicbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.molinaro.springbootionicbackend.domain.Categoria;
import com.molinaro.springbootionicbackend.domain.Produto;
import com.molinaro.springbootionicbackend.repositories.CategoriaRepository;
import com.molinaro.springbootionicbackend.repositories.ProdutoRepository;
import com.molinaro.springbootionicbackend.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		
		Produto obj = produtoRepository.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFoundException("Não existe Catedoria com id: #" + id);
		}
		
		return obj;
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		List<Categoria> categorias = categoriaRepository.findAll(ids);
		
		return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
}
