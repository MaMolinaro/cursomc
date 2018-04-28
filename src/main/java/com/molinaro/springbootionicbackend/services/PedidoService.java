package com.molinaro.springbootionicbackend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.molinaro.springbootionicbackend.domain.Cliente;
import com.molinaro.springbootionicbackend.domain.ItemPedido;
import com.molinaro.springbootionicbackend.domain.PagamentoComBoleto;
import com.molinaro.springbootionicbackend.domain.Pedido;
import com.molinaro.springbootionicbackend.domain.enums.EstadoPagamento;
import com.molinaro.springbootionicbackend.repositories.ClienteRepository;
import com.molinaro.springbootionicbackend.repositories.ItemPedidoRepository;
import com.molinaro.springbootionicbackend.repositories.PagamentoRepository;
import com.molinaro.springbootionicbackend.repositories.PedidoRepository;
import com.molinaro.springbootionicbackend.repositories.ProdutoRepository;
import com.molinaro.springbootionicbackend.security.UserSpringSecurity;
import com.molinaro.springbootionicbackend.services.exceptions.AuthorizationException;
import com.molinaro.springbootionicbackend.services.exceptions.ObjectNotFoundException;

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
	private ClienteRepository clienteRepository;

	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private EmailService emailService;
	

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
		obj.setCliente(clienteRepository.findOne(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItems()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoRepository.findOne(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.save(obj.getItems());
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSpringSecurity user = UserService.authenticated();
		
		if (user == null) {
			throw new AuthorizationException("Acesso Negado!");
		}
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Cliente cliente = clienteRepository.findOne(user.getId());
		
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}
}
