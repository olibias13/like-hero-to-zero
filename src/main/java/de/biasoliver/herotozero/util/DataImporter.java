package de.biasoliver.herotozero.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.biasoliver.herotozero.model.Country;
import de.biasoliver.herotozero.model.Emission;
import de.biasoliver.herotozero.model.User;
import jakarta.persistence.EntityManager;

public class DataImporter {

	private static final String CSV_PFAD = "data/co2-emissions.csv";

	public void importIfEmpty() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Long anzahl = em.createQuery("select count(c) from Country c", Long.class).getSingleResult();
			if (anzahl > 0) {
				return;
			}

			InputStream in = DataImporter.class.getClassLoader().getResourceAsStream(CSV_PFAD);
			if (in == null) {
				throw new IllegalStateException(CSV_PFAD + " nicht gefunden");
			}

			em.getTransaction().begin();
			Map<String, Country> laender = new HashMap<>();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				reader.readLine(); // Kopfzeile ueberspringen
				String zeile;
				while ((zeile = reader.readLine()) != null) {
					if (zeile.isBlank()) {
						continue;
					}
					String[] f = zerlege(zeile);
					String iso = f[1];

					Country land = laender.get(iso);
					if (land == null) {
						land = new Country();
						land.setIsoCode(iso);
						land.setName(f[0]);
						em.persist(land);
						laender.put(iso, land);
					}

					Emission emission = new Emission();
					emission.setCountry(land);
					emission.setYear(Integer.parseInt(f[2]));
					emission.setCo2(Double.parseDouble(f[3]));
					em.persist(emission);
				}
			} catch (IOException e) {
				throw new IllegalStateException("CSV konnte nicht gelesen werden", e);
			}

			em.getTransaction().commit();
		} catch (RuntimeException e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}
	
	public void createDemoUserIfMissing() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Long anzahl = em.createQuery("select count(u) from User u", Long.class).getSingleResult();
			if (anzahl == 0) {
				em.getTransaction().begin();
				User user = new User();
				user.setUsername("forscher");
				user.setPasswordHash(PasswordUtil.hash("demo1234"));
				em.persist(user);
				em.getTransaction().commit();
			}
		} finally {
			em.close();
		}
	}

	// Zerlegt eine CSV-Zeile und beachtet Anfuehrungszeichen
	private static String[] zerlege(String zeile) {
		List<String> felder = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean inAnfuehrung = false;
		for (char ch : zeile.toCharArray()) {
			if (ch == '"') {
				inAnfuehrung = !inAnfuehrung;
			} else if (ch == ',' && !inAnfuehrung) {
				felder.add(sb.toString());
				sb.setLength(0);
			} else {
				sb.append(ch);
			}
		}
		felder.add(sb.toString());
		return felder.toArray(new String[0]);
	}
}