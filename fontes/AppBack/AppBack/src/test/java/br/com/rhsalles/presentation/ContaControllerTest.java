package br.com.rhsalles.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.com.rhsalles.application.dtos.ContaDto;
import br.com.rhsalles.application.dtos.JwtDto;
import br.com.rhsalles.application.dtos.LoginDto;
import br.com.rhsalles.application.services.ContaService;
import br.com.rhsalles.domain.entities.Conta;
import br.com.rhsalles.infrastructure.security.JwtProvider;

@ExtendWith(MockitoExtension.class)
class ContaControllerTest {

	@Mock
    private ContaService contaService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
    }
    
    @Test
    void authenticateUser_validCredentials_returnJwtDto() {
        LoginDto loginDto = new LoginDto("teste", "teste123");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())))
                .thenReturn(authentication);
        when(jwtProvider.generateJwt(authentication))
                .thenReturn("teste.jwt.token");

        ResponseEntity<JwtDto> response = contaController.authenticateUser(loginDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("teste.jwt.token", response.getBody().getToken());
    }

    @Test
    void cadastrarConta_validDto_returnCreated() {
        ContaDto contaDto = new ContaDto();
        ResponseEntity<Object> response = contaController.cadastrarConta(contaDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Conta cadastrada com sucesso!", response.getBody());
    }

    @Test
    void listarContasAPagar_validParameters_returnPageOfContas() {
        Page<Conta> contasPage = new PageImpl<>(List.of(new Conta()));
        when(contaService.listarContasAPagar(null, LocalDate.now(), "123"))
                .thenReturn(contasPage);

        ResponseEntity<Page<Conta>> response = contaController.listarContasAPagar(null, LocalDate.now(), "123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contasPage, response.getBody());
    }

    @Test
    void obterContaPorId_existingId_returnConta() {
        Long id = 1L;
        Conta conta = new Conta();
        conta.setId(id);
        Optional<Conta> contaOptional = Optional.of(conta);
        when(contaService.findById(id))
                .thenReturn(contaOptional);

        ResponseEntity<Object> response = contaController.obterContaPorId(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void obterValorTotalPagoPorPeriodo_validDates_returnTotal() {
        LocalDate dataInicio = LocalDate.now().minusDays(7);
        LocalDate dataFim = LocalDate.now();

        BigDecimal total = new BigDecimal("1000.00");
        when(contaService.obterValorTotalPagoPorPeriodo(dataInicio, dataFim))
                .thenReturn(total);

        ResponseEntity<Object> response = contaController.obterValorTotalPagoPorPeriodo(dataInicio, dataFim);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(total, response.getBody());
    }

}
