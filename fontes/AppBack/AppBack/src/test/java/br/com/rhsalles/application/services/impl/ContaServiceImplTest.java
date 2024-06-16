package br.com.rhsalles.application.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.rhsalles.domain.entities.Conta;
import br.com.rhsalles.infrastructure.repositories.ContaRepository;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({ContaServiceImpl.class})
class ContaServiceImplTest {

	@Mock
	private ContaRepository contaRepository;
	
	@InjectMocks
	private ContaServiceImpl contaService;
	
	@BeforeEach
	void setUp() {
	}
	
	@Test
    void testCadastrarConta() {
        Conta conta = new Conta();
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta contaSalva = contaService.cadastrarConta(conta);
        assertNotNull(contaSalva);
    }

	@Test
    void testAtualizarConta() {
        Conta conta = new Conta();
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta contaAtualizada = contaService.atualizarConta(conta);
        assertNotNull(contaAtualizada);
    }

	@Test
	void testListarContasAPagar() {
	    Pageable pageable = Pageable.unpaged();
	    LocalDate dataVencimento = LocalDate.now();
	    String descricao = "Teste";
	
	    Page<Conta> contas = new PageImpl<>(List.of(new Conta()));
	    when(contaRepository.findByDescricaoAndDataVencimentoAndDataPagamento(pageable, descricao, dataVencimento, null))
	            .thenReturn(contas);
	
	    Page<Conta> resultado = contaService.listarContasAPagar(pageable, dataVencimento, descricao);
	    assertNotNull(resultado);
	}
	
	@Test
    void testObterValorTotalPagoPorPeriodo() {
        LocalDate inicioPagamento = LocalDate.now().minusDays(7);
        LocalDate fimPagamento = LocalDate.now();

        var c1 =  new Conta();
        c1.setValor(new BigDecimal(100));
        var c2 =  new Conta();
        c2.setValor(new BigDecimal(50));
        
        List<Conta> contas = List.of(c1, c2);
        
        when(contaRepository.findByDataPagamentoBetween(inicioPagamento, fimPagamento)).thenReturn(contas);

        BigDecimal total = contaService.obterValorTotalPagoPorPeriodo(inicioPagamento, fimPagamento);
        assertNotNull(total);
        assertEquals(total, new BigDecimal(150));
    }

	@Test
    void testFindById() {
        Long id = 1L;
        Conta conta = new Conta();
        when(contaRepository.findById(id)).thenReturn(Optional.of(conta));

        Optional<Conta> resultado = contaService.findById(id);
        assertTrue(resultado.isPresent());
    }

	@Test
    void testSaveAll() {
        List<Conta> contas = List.of(new Conta(), new Conta());

        contaService.saveAll(contas);
        verify(contaRepository, times(1)).saveAll(contas);
    }
}
