package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class RegionDAOImpl implements RegionDAO {
    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);
    @PersistenceContext private EntityManager entityManager;

    @Override public List<Region> listAllRegions() {
        return entityManager.createQuery("SELECT r FROM Region r", Region.class).getResultList();
    }
    @Override public List<Region> listRegionsPage(int page, int size, String sortField, String sortDir) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Region> query = cb.createQuery(Region.class);
        Root<Region> root = query.from(Region.class);
        var sortPath = switch (sortField) {
            case "id" -> root.get("id");
            case "code" -> root.get("code");
            case "name" -> root.get("name");
            default -> { logger.warn("Unknown sortField '{}' for Region, defaulting to 'name'.", sortField); yield root.get("name"); }
        };
        query.select(root).orderBy("desc".equalsIgnoreCase(sortDir) ? cb.desc(sortPath) : cb.asc(sortPath));
        return entityManager.createQuery(query).setFirstResult(page * size).setMaxResults(size).getResultList();
    }
    @Override public long countRegions() {
        return entityManager.createQuery("SELECT COUNT(r) FROM Region r", Long.class).getSingleResult();
    }
    @Override public Region getRegionById(Long id) { return entityManager.find(Region.class, id); }
    @Override public void insertRegion(Region region) { entityManager.persist(region); }
    @Override public void updateRegion(Region region) { entityManager.merge(region); }
    @Override public void deleteRegion(Long id) { Region region=entityManager.find(Region.class,id); if(region!=null) entityManager.remove(region); }
    @Override public boolean existsRegionByCode(String code) {
        return entityManager.createQuery("SELECT COUNT(r) FROM Region r WHERE UPPER(r.code)=:code", Long.class)
                .setParameter("code", code.toUpperCase()).getSingleResult()>0;
    }
    @Override public boolean existsRegionByCodeAndNotId(String code, Long id) {
        return entityManager.createQuery("SELECT COUNT(r) FROM Region r WHERE UPPER(r.code)=:code AND r.id<>:id", Long.class)
                .setParameter("code", code.toUpperCase()).setParameter("id", id).getSingleResult()>0;
    }
}
