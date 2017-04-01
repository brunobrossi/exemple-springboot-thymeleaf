package com.algaworks.cobranca.model;

public enum StatusTitulo {
	
	PENDENTE("Pendente"),
	RECEBBIDO("Recebido");
	
	private String descricao;
	
	StatusTitulo(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao(){
		return descricao;
	}
	
}
