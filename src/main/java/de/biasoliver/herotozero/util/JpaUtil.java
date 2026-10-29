package de.biasoliver.herotozero.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

	private static EntityManagerFactory emf;

	private JpaUtil() {
	}

	public static synchronized EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			Properties props = new Properties();
			try (InputStream in = JpaUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
				if (in == null) {
					throw new IllegalStateException(
							"db.properties nicht gefunden. Bitte aus db.properties.example anlegen.");
				}
				props.load(in);
			} catch (IOException e) {
				throw new IllegalStateException("db.properties konnte nicht gelesen werden", e);
			}

			Map<String, String> overrides = new HashMap<>();
			overrides.put("jakarta.persistence.jdbc.user", props.getProperty("db.user"));
			overrides.put("jakarta.persistence.jdbc.password", props.getProperty("db.password"));

			emf = Persistence.createEntityManagerFactory("herotozero", overrides);
		}
		return emf;
	}

	public static synchronized void close() {
		if (emf != null) {
			emf.close();
			emf = null;
		}
	}
}