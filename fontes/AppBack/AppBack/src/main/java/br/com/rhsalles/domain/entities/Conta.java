package br.com.rhsalles.domain.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.rhsalles.domain.enums.SituacaoConta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "conta")
public class Conta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(nullable = false)
    private LocalDate dataVencimento;

    @Column
    private LocalDate dataPagamento;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoConta situacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
