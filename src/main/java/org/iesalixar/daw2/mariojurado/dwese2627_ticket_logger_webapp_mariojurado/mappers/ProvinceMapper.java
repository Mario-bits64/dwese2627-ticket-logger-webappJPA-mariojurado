package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.mappers;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.dtos.*;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.*;
import java.util.List;
public final class ProvinceMapper {
 private ProvinceMapper() {}
 public static ProvinceDTO toDTO(Province e){if(e==null)return null; return new ProvinceDTO(e.getId(),e.getCode(),e.getName(),e.getRegion()==null?null:e.getRegion().getName());}
 public static List<ProvinceDTO> toDTOList(List<Province> es){return es==null?List.of():es.stream().map(ProvinceMapper::toDTO).toList();}
 public static ProvinceDetailDTO toDetailDTO(Province e){if(e==null)return null;return new ProvinceDetailDTO(e.getId(),e.getCode(),e.getName(),RegionMapper.toDTO(e.getRegion()));}
 public static ProvinceUpdateDTO toUpdateDTO(Province e){if(e==null)return null;return new ProvinceUpdateDTO(e.getId(),e.getCode(),e.getName(),e.getRegion()==null?null:e.getRegion().getId());}
 public static Province toEntity(ProvinceCreateDTO d){if(d==null)return null;Province p=new Province();p.setCode(d.getCode());p.setName(d.getName());p.setRegion(new Region());p.getRegion().setId(d.getRegionId());return p;}
 public static Province toEntity(ProvinceUpdateDTO d){if(d==null)return null;Province p=toEntity(new ProvinceCreateDTO(d.getId(),d.getCode(),d.getName(),d.getRegionId()));p.setId(d.getId());return p;}
 public static void copyToExistingEntity(ProvinceUpdateDTO d, Province p){if(d!=null&&p!=null){p.setCode(d.getCode());p.setName(d.getName());p.setRegion(new Region());p.getRegion().setId(d.getRegionId());}}
}
