package br.com.rhsalles.application.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.rhsalles.application.services.ContaService;
import br.com.rhsalles.domain.entities.Conta;
import br.com.rhsalles.infrastructure.repositories.ContaRepository;

@Service
public class ContaServiceImpl implements ContaService {

	@Autowired
	ContaRepository contaRepository;
	
	@Override
	public Conta cadastrarConta(Conta conta) {
		return contaRepository.save(conta);
	}

	@Override
	public Conta atualizarConta(Conta conta) {
		return contaRepository.save(conta);
	}

	@Override
	public Page<Conta> listarContasAPagar(Pageable pageable, LocalDate dataVencimento, String descricao) {
		Page<Conta> contas;
		if(descricao == null || descricao.isBlank()) {
			contas = contaRepository.findByDataVencimentoAndDataPagamento(pageable, dataVencimento, null);	
		} else {
			contas = contaRepository.findByDescricaoAndDataVencimentoAndDataPagamento(pageable, descricao, dataVencimento, null);
		}
		
		
		return contas;
	}

	@Override
	public BigDecimal obterValorTotalPagoPorPeriodo(LocalDate inicioPagamento, LocalDate fimPagamento) {
		List<Conta> contas = contaRepository.findByDataPagamentoBetween(inicioPagamento, fimPagamento);
		return contas.stream().map(Conta::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public Optional<Conta> findById(Long id) {
		return contaRepository.findById(id);
	}

	@Override
	public void saveAll(List<Conta> contas) {
		contaRepository.saveAll(contas);
	}

}
