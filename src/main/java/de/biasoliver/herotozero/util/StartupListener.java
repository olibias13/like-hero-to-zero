package de.biasoliver.herotozero.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new DataImporter().importIfEmpty();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JpaUtil.close();
	}
}
