package com.molinaro.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.molinaro.cursomc.domain.Cliente;
import com.molinaro.cursomc.domain.enums.TipoCliente;
import com.molinaro.cursomc.dto.ClienteNewDTO;
import com.molinaro.cursomc.repositories.ClienteRepository;
import com.molinaro.cursomc.resources.exceptions.FieldMessage;
import com.molinaro.cursomc.services.validation.utils.BR;
 
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> { 
	
	@Autowired
	private ClienteRepository clienteRepository;
 
    @Override     
    public void initialize(ClienteInsert ann) {     }       
    
    @Override     
    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) { 
 
        List<FieldMessage> list = new ArrayList<>();   
        
        if (objDto.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCodigo()) && 
        	!BR.isValidCPF(objDto.getCpfOrCnpj())) {
        	list.add(new FieldMessage("cpfOrCnpj", "CPF inválido"));
        }
        
        if (objDto.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && 
            	!BR.isValidCNPJ(objDto.getCpfOrCnpj())) {
            	list.add(new FieldMessage("cpfOrCnpj", "CNPJ inválido"));
        }
        
        Cliente cliente = clienteRepository.findByEmail(objDto.getEmail());
        
        if (cliente != null) {
        	list.add(new FieldMessage("email", "E-Mail já existe"));
        }

        
        for (FieldMessage e : list) {             
        	context.disableDefaultConstraintViolation();             
        	context.buildConstraintViolationWithTemplate(e.getMessage())
        		.addPropertyNode(e.getFieldName())
        		.addConstraintViolation();         
        }         
        
        return list.isEmpty();     
     } 
} 