package br.com.fiap.ms_pedidos.controller;

import br.com.fiap.ms_pedidos.dto.PedidoDTO;
import br.com.fiap.ms_pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // testando a distribuição de carga
    // devolve a porta de instância que está rodando
    @GetMapping("/port")
    public ResponseEntity<String> getPort(@Value("${local.server.port}") String port){
        String map = String.format("Requisição da instância recebida na porta: %s", port);
        return ResponseEntity.ok(map);
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> insert(
           @RequestBody @Valid PedidoDTO pedidoDTO
    ){
        var pedido = pedidoService.insert(pedidoDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(pedido.getId())
                .toUri();

        return ResponseEntity.created(uri).body(pedido);
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> findAll(){
        var pedidos = pedidoService.findAll();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(@PathVariable Long id){
        var pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedido);
    }
}
