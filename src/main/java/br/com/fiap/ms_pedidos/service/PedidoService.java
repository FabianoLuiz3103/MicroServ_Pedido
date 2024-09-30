package br.com.fiap.ms_pedidos.service;

import br.com.fiap.ms_pedidos.dto.PedidoDTO;
import br.com.fiap.ms_pedidos.dto.StatusDTO;
import br.com.fiap.ms_pedidos.model.Pedido;
import br.com.fiap.ms_pedidos.model.Status;
import br.com.fiap.ms_pedidos.repository.PedidoRepository;
import br.com.fiap.ms_pedidos.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Transactional
    public PedidoDTO insert(PedidoDTO pedidoDTO){
        var pedido = modelMapper.map(pedidoDTO, Pedido.class);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(Status.REALIZADO);
        pedido.getItens().forEach(item -> item.setPedido(pedido));
        pedidoRepository.save(pedido);

        return modelMapper.map(pedido, PedidoDTO.class);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAll(){

        return pedidoRepository.findAll().stream()
                .map(pedido -> modelMapper
                        .map(pedido, PedidoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoDTO findById(Long id){
        var pedido = pedidoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id)
        );

        return modelMapper.map(pedido, PedidoDTO.class);
    }


    @Transactional
    public void aprovarPagamentoDoPedido(Long id){
        Pedido pedido = pedidoRepository.getPedidoByIdWithItens(id);
        if(pedido == null){
            throw new ResourceNotFoundException(id);
        }

        pedido.setStatus(Status.PAGO);
        pedidoRepository.updateStatus(Status.PAGO, pedido);
    }

    @Transactional
    public PedidoDTO updatePedidoStatus(Long id, StatusDTO statusDTO){

        Pedido pedido = pedidoRepository.getPedidoByIdWithItens(id);
        if(pedido == null){
            throw new ResourceNotFoundException(id);
        }

        pedido.setStatus(statusDTO.getStatus());
        pedidoRepository.updateStatus(statusDTO.getStatus(), pedido);
        return modelMapper.map(pedido, PedidoDTO.class);
    }
}
