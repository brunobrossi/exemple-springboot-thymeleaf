package com.algaworks.cobranca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.Titulos;
import com.algaworks.cobranca.repository.filter.TituloFilter;

@Service
public class CadastroTituloService {

	@Autowired
	private Titulos titulos;

	public void save(Titulo titulo) {

		try {
			titulos.save(titulo);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato da data inválida");
		}

	}

	public List<Titulo> findByDescricaoContaining(TituloFilter filtro) {
		
		String descricao = filtro.descricao == null ? "%" : filtro.getDescricao(); 
		return titulos.findByDescricaoContaining(descricao);
	}

	public void delete(Long codigo) {
		titulos.delete(codigo);
	}

	public String receber(Long codigo) {

		Titulo titulo = titulos.findOne(codigo);
		titulo.setStatus(StatusTitulo.RECEBBIDO);
		titulos.save(titulo);

		return StatusTitulo.RECEBBIDO.getDescricao();

	}

}
