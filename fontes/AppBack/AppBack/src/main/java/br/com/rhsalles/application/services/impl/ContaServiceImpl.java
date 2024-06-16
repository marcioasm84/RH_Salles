package br.com.rhsalles.application.services.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import br.com.rhsalles.application.dtos.ContaImportadaDto;
import br.com.rhsalles.application.services.ContaService;
import br.com.rhsalles.domain.entities.Conta;
import br.com.rhsalles.infrastructure.repositories.ContaRepository;
import br.com.rhsalles.infrastructure.repositories.utils.Util;
import br.com.rhsalles.infrastructure.security.publishers.ImportaContaEventPublisher;

@Service
public class ContaServiceImpl implements ContaService {

	@Autowired
	ContaRepository contaRepository;
	@Autowired
	ImportaContaEventPublisher importaContaEventPublisher;
	

	@Value(value = "${app.uploadDir}")
	private String dirUpload;
	
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
	public void importaContas(MultipartFile arquivoCsv) {
		try {
			String nomeArquivo = Util.saveFile(arquivoCsv, dirUpload);
			importaContaEventPublisher.publishImportaContaEvent(nomeArquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void processarImportaContas(String pathArquivo) {
		
		try { 
			FileInputStream arquivoCsv = new FileInputStream(dirUpload+"/"+pathArquivo);
    		CsvMapper csvMapper = new CsvMapper();
    		CsvSchema schema = CsvSchema.emptySchema().withHeader();
    		MappingIterator<ContaImportadaDto> contaMappingIterator = csvMapper.readerFor(ContaImportadaDto.class).
    																	with(schema).
    																	readValues(arquivoCsv);
            List<ContaImportadaDto> contasImportadas = contaMappingIterator.readAll();
            List<Conta> contas = contasImportadas.stream().map(c -> c.toConta()).collect(Collectors.toList());
            this.saveAll(contas);
            
            Util.moveFileToDir( dirUpload+"/"+pathArquivo, dirUpload+"/processado/" + pathArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void saveAll(List<Conta> contas) {
		contaRepository.saveAll(contas);
	}

}
