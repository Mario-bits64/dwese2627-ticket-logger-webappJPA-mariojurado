package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.mappers;

import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.dtos.*;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.*;
import java.util.List;

public final class RegionMapper {
    private RegionMapper() {}
    public static RegionDTO toDTO(Region e) {
        if(e==null)return null; return new RegionDTO(e.getId(),e.getCode(),e.getName());
    }
    public static List<RegionDTO> toDTOList(List<Region> es) { return es==null?List.of():es.stream().map(RegionMapper::toDTO).toList(); }
    public static RegionDetailDTO toDetailDTO(Region e) {
        if(e==null)return null; return new RegionDetailDTO(e.getId(),e.getCode(),e.getName(),
            e.getProvinces()==null?List.of():e.getProvinces().stream().map(p->new ProvinceDTO(p.getId(),p.getCode(),p.getName(),null)).toList());
    }
    public static RegionUpdateDTO toUpdateDTO(Region e) { return e==null?null:new RegionUpdateDTO(e.getId(),e.getCode(),e.getName()); }
    public static Region toEntity(RegionCreateDTO d) { return d==null?null:new Region(d.getCode(),d.getName()); }
    public static Region toEntity(RegionUpdateDTO d) {
        if (d == null) return null;
        Region entity = new Region(d.getCode(), d.getName());
        entity.setId(d.getId());
        return entity;
    }
    public static void copyToExistingEntity(RegionUpdateDTO d, Region e) { if(d!=null&&e!=null){e.setCode(d.getCode());e.setName(d.getName());} }
}
