package br.com.fiap.ms_pedidos.dto;

import br.com.fiap.ms_pedidos.model.ItemDoPedido;
import br.com.fiap.ms_pedidos.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PedidoDTO {

    private Long id;
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private StatusDTO status;

    private List<ItemDoPedidoDTO> itens = new ArrayList<>();
}
