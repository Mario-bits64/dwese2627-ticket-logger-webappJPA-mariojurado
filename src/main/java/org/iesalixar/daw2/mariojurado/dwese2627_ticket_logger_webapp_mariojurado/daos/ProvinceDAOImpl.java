package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class ProvinceDAOImpl implements ProvinceDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);
    @PersistenceContext private EntityManager entityManager;

    @Override public List<Province> listAllProvinces() {
        return entityManager.createQuery("SELECT p FROM Province p JOIN FETCH p.region", Province.class).getResultList();
    }
    @Override public List<Province> listProvincesPage(int page, int size, String sortField, String sortDir) {
        CriteriaBuilder cb=entityManager.getCriteriaBuilder();
        CriteriaQuery<Province> query=cb.createQuery(Province.class);
        Root<Province> root=query.from(Province.class);
        root.fetch("region", JoinType.INNER);
        var sortPath=switch(sortField) {
            case "id" -> root.get("id");
            case "code" -> root.get("code");
            case "name" -> root.get("name");
            case "regionName" -> root.join("region", JoinType.INNER).get("name");
            default -> { logger.warn("Unknown sortField '{}' for Province, defaulting to 'name'.", sortField); yield root.get("name"); }
        };
        query.select(root).distinct(true).orderBy("desc".equalsIgnoreCase(sortDir)?cb.desc(sortPath):cb.asc(sortPath));
        return entityManager.createQuery(query).setFirstResult(page*size).setMaxResults(size).getResultList();
    }
    @Override public long countProvinces() { return entityManager.createQuery("SELECT COUNT(p) FROM Province p",Long.class).getSingleResult(); }
    @Override public Province getProvinceById(Long id) { return entityManager.find(Province.class,id); }
    @Override public void insertProvince(Province province) { entityManager.persist(province); }
    @Override public void updateProvince(Province province) { entityManager.merge(province); }
    @Override public void deleteProvince(Long id) { Province p=entityManager.find(Province.class,id); if(p!=null) entityManager.remove(p); }
    @Override public boolean existsProvinceByCode(String code) {
        return entityManager.createQuery("SELECT COUNT(p) FROM Province p WHERE UPPER(p.code)=:code",Long.class)
                .setParameter("code",code.toUpperCase()).getSingleResult()>0;
    }
    @Override public boolean existsProvinceByCodeAndNotId(String code, Long id) {
        return entityManager.createQuery("SELECT COUNT(p) FROM Province p WHERE UPPER(p.code)=:code AND p.id<>:id",Long.class)
                .setParameter("code",code.toUpperCase()).setParameter("id",id).getSingleResult()>0;
    }
}
