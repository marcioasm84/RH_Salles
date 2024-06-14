package br.com.rhsalles.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.rhsalles.domain.enums.SituacaoConta;

public class ContaDto {

    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String descricao;
    private SituacaoConta situacao;

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public SituacaoConta getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoConta situacao) {
		this.situacao = situacao;
	}
    
}
