package br.com.rhsalles.presentation;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import br.com.rhsalles.application.dtos.ContaDto;
import br.com.rhsalles.application.dtos.ContaImportadaDto;
import br.com.rhsalles.application.dtos.JwtDto;
import br.com.rhsalles.application.dtos.LoginDto;
import br.com.rhsalles.application.services.ContaService;
import br.com.rhsalles.domain.entities.Conta;
import br.com.rhsalles.domain.enums.SituacaoConta;
import br.com.rhsalles.infrastructure.security.JwtProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/conta")
public class ContaController {

	@Autowired
    private ContaService contaService;
	
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;
    
	@GetMapping
	public ResponseEntity<Object> teste() {
		return ResponseEntity.status(HttpStatus.OK).body("Entrou");
	}
	
	@PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }
	
	@PostMapping
    public ResponseEntity<Object> cadastrarConta(@RequestBody ContaDto contaDto) {
		var conta = new Conta();
		BeanUtils.copyProperties(contaDto, conta);
        contaService.cadastrarConta(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body("Conta cadastrada com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarConta(@PathVariable Long id, @RequestBody ContaDto contaDto) {
    	Optional<Conta> contaOptional = contaService.findById(id);
		if(!contaOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta inexistente!");
		}
    	var conta = contaOptional.get();
		BeanUtils.copyProperties(contaDto, conta);
    	contaService.atualizarConta(conta);
        return ResponseEntity.status(HttpStatus.OK).body("Conta atualizada com sucesso!");
    }

    @PutMapping("/{id}/alterarSituacao")
    public ResponseEntity<Object> alterarSituacaoConta(@PathVariable Long id, @RequestParam("situacao") SituacaoConta situacao) {
    	Optional<Conta> contaOptional = contaService.findById(id);
		if(!contaOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta inexistente!");
		}
		var conta = contaOptional.get();
		conta.setSituacao(situacao);
		if(situacao.equals(SituacaoConta.PAGA)) {
			conta.setDataPagamento(LocalDate.now());
		}
		contaService.atualizarConta(conta);
        return ResponseEntity.status(HttpStatus.OK).body("Situação da conta alterada com sucesso!");
    }

    @GetMapping("/listar-contas-a-pagar")
    public ResponseEntity<Page<Conta>> listarContasAPagar(
    							@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
    							@RequestParam(required = false) LocalDate dataVencimento, @RequestParam(required = false) String descricao) {
        Page<Conta> contas = contaService.listarContasAPagar(pageable, dataVencimento, descricao);
        return ResponseEntity.status(HttpStatus.OK).body(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obterContaPorId(@PathVariable Long id) {
    	Optional<Conta> contaOptional = contaService.findById(id);
		if(!contaOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta inexistente!");
		}
		var conta = contaOptional.get();
		return ResponseEntity.status(HttpStatus.OK).body(conta);
    }

    @GetMapping("/total-pago")
    public ResponseEntity<Object> obterValorTotalPagoPorPeriodo(@RequestParam LocalDate dataInicioPagamento, 
    															@RequestParam LocalDate dataFimPagamento) {
    	BigDecimal total = contaService.obterValorTotalPagoPorPeriodo(dataInicioPagamento, dataFimPagamento);
        return ResponseEntity.status(HttpStatus.OK).body(total);
    }
    
    @PostMapping("/importar")
    public ResponseEntity<Object> importarContas(@RequestParam("arquivo") MultipartFile arquivoCsv) {
    	if(arquivoCsv.isEmpty()) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não informado!");
    	}
    	try { 
    		CsvMapper csvMapper = new CsvMapper();
    		CsvSchema schema = CsvSchema.emptySchema().withHeader();
    		MappingIterator<ContaImportadaDto> contaMappingIterator = csvMapper.readerFor(ContaImportadaDto.class).
    																	with(schema).
    																	readValues(arquivoCsv.getInputStream());
            List<ContaImportadaDto> contasImportadas = contaMappingIterator.readAll();
            List<Conta> contas = contasImportadas.stream().map(c -> c.toConta()).collect(Collectors.toList());
            contaService.saveAll(contas);
    		
            return ResponseEntity.status(HttpStatus.OK).body("Arquivo recebido e processado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o arquivo. Verifique o formado do arquivo: " + e.getMessage());
        }
    	
    }
}
