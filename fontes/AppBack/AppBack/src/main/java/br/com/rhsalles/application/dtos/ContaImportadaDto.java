package br.com.rhsalles.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.rhsalles.domain.entities.Conta;
import br.com.rhsalles.domain.enums.SituacaoConta;

public class ContaImportadaDto {

    private String dataVencimento;
    private String dataPagamento;
    private String valor;
    private String descricao;
    private SituacaoConta situacao;
    
	public String getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public String getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
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
	public Conta toConta() {
		Conta contaDto = new Conta();
		if(dataPagamento != null && !dataPagamento.isEmpty())
			contaDto.setDataPagamento(LocalDate.parse(dataPagamento));
		if(dataVencimento != null && !dataVencimento.isEmpty())
			contaDto.setDataVencimento(LocalDate.parse(dataVencimento));
		contaDto.setDescricao(descricao);
		contaDto.setSituacao(situacao);
		if(valor != null && !valor.isEmpty())
			contaDto.setValor(new BigDecimal(valor));
		return contaDto;
	}
}
