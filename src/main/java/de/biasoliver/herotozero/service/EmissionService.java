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
}