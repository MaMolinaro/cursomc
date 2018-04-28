package com.molinaro.springbootionicbackend.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.molinaro.springbootionicbackend.domain.Cliente;
import com.molinaro.springbootionicbackend.dto.ClienteDTO;
import com.molinaro.springbootionicbackend.repositories.ClienteRepository;
import com.molinaro.springbootionicbackend.resources.exceptions.FieldMessage;

 
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> { 
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ClienteRepository clienteRepository;
 
    @Override     
    public void initialize(ClienteUpdate ann) {     }       
    
    @Override     
    public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) { 
 
    	@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    	
    	Integer uriId = Integer.parseInt(map.get("id"));
    	
        List<FieldMessage> list = new ArrayList<>();   
                
        Cliente cliente = clienteRepository.findByEmail(objDto.getEmail());
        
        if (cliente != null && !cliente.getId().equals(uriId)) {
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