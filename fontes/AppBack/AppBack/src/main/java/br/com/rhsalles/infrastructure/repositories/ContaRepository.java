package br.com.rhsalles.infrastructure.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rhsalles.domain.entities.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	Page<Conta> findByDescricaoAndDataVencimentoAndDataPagamento(Pageable pageable, 
			String descricao, 
			LocalDate dataVencimento,
			LocalDate dataPagamento);

	Page<Conta> findByDataVencimentoAndDataPagamento(Pageable pageable, LocalDate dataVencimento, LocalDate dataPagamento);

	List<Conta> findByDataPagamentoBetween(LocalDate inicioPagamento, LocalDate fimPagamento);

}
