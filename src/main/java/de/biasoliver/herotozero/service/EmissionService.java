package de.biasoliver.herotozero.service;

import java.io.Serializable;
import java.util.List;

import de.biasoliver.herotozero.model.Country;
import de.biasoliver.herotozero.model.Emission;
import de.biasoliver.herotozero.util.JpaUtil;
import jakarta.persistence.EntityManager;

public class EmissionService implements Serializable {

	public List<Country> findAllCountries() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			return em.createQuery("select c from Country c order by c.name", Country.class).getResultList();
		} finally {
			em.close();
		}
	}

	public Emission findLatestEmission(Long countryId) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Emission> result = em
					.createQuery("select e from Emission e where e.country.id = :id order by e.year desc",
							Emission.class)
					.setParameter("id", countryId).setMaxResults(1).getResultList();
			return result.isEmpty() ? null : result.get(0);
		} finally {
			em.close();
		}
	}
	public List<Emission> findEmissions(Long countryId) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			return em.createQuery("select e from Emission e where e.country.id = :id order by e.year desc",
					Emission.class).setParameter("id", countryId).getResultList();
		} finally {
			em.close();
		}
	}

	/** Liefert true, wenn ein neuer Wert angelegt wurde, false bei Korrektur. */
	public boolean saveOrUpdate(Long countryId, int year, double co2) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			List<Emission> existing = em
					.createQuery("select e from Emission e where e.country.id = :id and e.year = :year",
							Emission.class)
					.setParameter("id", countryId).setParameter("year", year).getResultList();
			boolean created;
			if (existing.isEmpty()) {
				Emission e = new Emission();
				e.setCountry(em.getReference(Country.class, countryId));
				e.setYear(year);
				e.setCo2(co2);
				em.persist(e);
				created = true;
			} else {
				existing.get(0).setCo2(co2);
				created = false;
			}
			em.getTransaction().commit();
			return created;
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
	}
}