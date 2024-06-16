package br.com.rhsalles.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import br.com.rhsalles.domain.entities.Conta;

public interface ContaService {

	Conta cadastrarConta(Conta conta);

	Conta atualizarConta(Conta conta);

	Page<Conta> listarContasAPagar(Pageable pageable, LocalDate dataVencimento, String descricao);

	Optional<Conta> findById(Long id);

	BigDecimal obterValorTotalPagoPorPeriodo(LocalDate inicioPagamento, LocalDate fimPagamento);

	void saveAll(List<Conta> contas);

	void importaContas(MultipartFile arquivoCsv);

	void processarImportaContas(String pathArquivo);
}
