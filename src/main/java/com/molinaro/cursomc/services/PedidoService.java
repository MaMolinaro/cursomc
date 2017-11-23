package com.molinaro.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molinaro.cursomc.domain.ItemPedido;
import com.molinaro.cursomc.domain.PagamentoComBoleto;
import com.molinaro.cursomc.domain.Pedido;
import com.molinaro.cursomc.domain.enums.EstadoPagamento;
import com.molinaro.cursomc.repositories.ItemPedidoRepository;
import com.molinaro.cursomc.repositories.PagamentoRepository;
import com.molinaro.cursomc.repositories.PedidoRepository;
import com.molinaro.cursomc.repositories.ProdutoRepository;
import com.molinaro.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private BoletoService boletoService;

	public Pedido find(Integer id) {
		
		Pedido obj = pedidoRepository.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFoundException("Não existe Catedoria com id: #" + id);
		}
		
		return obj;
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItems()) {
			ip.setDesconto(0.00);
			ip.setPreco(produtoRepository.findOne(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.save(obj.getItems());
		return obj;
	}
}
