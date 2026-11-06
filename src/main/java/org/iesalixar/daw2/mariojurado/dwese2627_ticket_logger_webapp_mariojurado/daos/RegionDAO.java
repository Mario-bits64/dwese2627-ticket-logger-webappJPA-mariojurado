package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.daos;

import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.Region;
import java.util.List;

public interface RegionDAO {
    List<Region> listAllRegions();
    List<Region> listRegionsPage(int page, int size, String sortField, String sortDir);
    long countRegions();
    void insertRegion(Region region);
    void updateRegion(Region region);
    void deleteRegion(Long id);
    boolean existsRegionByCode(String code);
    boolean existsRegionByCodeAndNotId(String code, Long id);
    Region getRegionById(Long id);
}
