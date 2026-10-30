package de.biasoliver.herotozero.service;

import java.util.List;

import de.biasoliver.herotozero.model.User;
import de.biasoliver.herotozero.util.JpaUtil;
import jakarta.persistence.EntityManager;
import java.io.Serializable;

public class UserService implements Serializable {

	public User findByUsername(String username) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<User> result = em.createQuery("select u from User u where u.username = :name", User.class)
					.setParameter("name", username).getResultList();
			return result.isEmpty() ? null : result.get(0);
		} finally {
			em.close();
		}
	}
}