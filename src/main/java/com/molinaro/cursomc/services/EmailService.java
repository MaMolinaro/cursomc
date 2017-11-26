package com.molinaro.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.molinaro.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
