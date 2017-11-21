package com.molinaro.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.molinaro.cursomc.domain.enums.TipoCliente;
import com.molinaro.cursomc.dto.ClienteNewDTO;
import com.molinaro.cursomc.resources.exceptions.FieldMessage;
import com.molinaro.cursomc.services.validation.utils.BR;
 
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> { 
 
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

        
        for (FieldMessage e : list) {             
        	context.disableDefaultConstraintViolation();             
        	context.buildConstraintViolationWithTemplate(e.getMessage())
        		.addPropertyNode(e.getFieldName())
        		.addConstraintViolation();         
        }         
        
        return list.isEmpty();     
     } 
} 