package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico de lectura para Province.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDTO {
    private Long id;
    private String code;
    private String name;
    private String regionName;
}
