package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO reutilizable para crear y actualizar regiones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionUpdateDTO {

    @NotNull(message = "{msg.region.id.notNull}")
    private Long id;

    @NotBlank(message = "{msg.region.code.notEmpty}")
    @Size(max = 2, message = "{msg.region.code.size}")
    private String code;

    @NotBlank(message = "{msg.region.name.notEmpty}")
    @Size(max = 100, message = "{msg.region.name.size}")
    private String name;
}
