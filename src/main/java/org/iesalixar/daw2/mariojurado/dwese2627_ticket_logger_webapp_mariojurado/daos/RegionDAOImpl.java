package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.Region;

import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class RegionDAOImpl implements RegionDAO {

    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lista todas las regiones de la base de datos.
     * @return Lista de regiones
     * @throws SQLException
     */
    public List<Region> listAllRegions() {
        logger.info("Listing all regions from the database");
        String hql = "SELECT r FROM Region r";
        List<Region> regions = entityManager.createQuery(hql, Region.class).getResultList();
        logger.info("Retrieved {} regions from the database." + regions.size());
        return regions;
    }

    /**
     * Recupera una región por su ID.
     * @param id ID de la región a recuperar
     * @return Región encontrada o null si no existe
     */
    @Override
    public Region getRegionById(Long id) {
        logger.info("Retrieving region by id: {}", id);
        Region region = entityManager.find(Region.class, id);
        if (region != null) {
            logger.info("Region retrieved: {} - {}", region.getCode(), region.getName());
        } else {
            logger.warn("No region found with id: {}", id);
        }
        return region;
    }


    public void insertRegion(Region region) {
        logger.info("Inserting region with code: {} and name: {}", region.getCode(), region.getName());
        entityManager.persist(region);
        logger.info("Inserted region successful");
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas.
     *
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     */
    @Override
    public boolean existsRegionByCode(String code) {
        String hql = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code";
        Long count = entityManager.createQuery(hql, Long.class).setParameter("code", code.toUpperCase()).getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists: {}", code, exists);
        return exists;
    }

    @Override
    public boolean existsRegionByCodeAndNotId(String code, Long id) {
        logger.info("Checking if region with code: {} exists excluding id: {}", code, id);
        String hql = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code AND r.id != :id";
        Long count = entityManager.createQuery(hql, Long.class).setParameter("code", code.toUpperCase()).setParameter("id", id).getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }

    /**
     * Actualiza una región existente en la base de datos.
     * @param region Región a actualizar
     */
    public void updateRegion(Region region) throws SQLException {
        logger.info("Updating region with id: {}" , region.getId());
        entityManager.merge(region);
        logger.info("Updated region with id: {}" , region.getId());
    }

    @Override
    public void deleteRegion(Long id) throws SQLException {
        logger.info("Deleting region with id: {}" , id);
        Region region = entityManager.find(Region.class, id);
        if (region != null) {
            entityManager.remove(region);
            logger.info("Deleted region with id: {}", id);
        } else {
            logger.warn("Region with id: {} not found", id);
        }
    }
}