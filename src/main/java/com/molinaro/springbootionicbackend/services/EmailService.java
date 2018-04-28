package com.molinaro.springbootionicbackend.services;

import org.springframework.mail.SimpleMailMessage;

import com.molinaro.springbootionicbackend.domain.Cliente;
import com.molinaro.springbootionicbackend.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);

}
