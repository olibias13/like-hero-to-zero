package de.biasoliver.herotozero.controller;

import java.io.Serializable;
import java.util.List;

import de.biasoliver.herotozero.model.Country;
import de.biasoliver.herotozero.model.Emission;
import de.biasoliver.herotozero.service.EmissionService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {

	private EmissionService service = new EmissionService();
	private List<Country> countries;
	private Long countryId;
	private Integer year;
	private Double co2;
	private List<Emission> emissions;

	public List<Country> getCountries() {
		if (countries == null) {
			countries = service.findAllCountries();
		}
		return countries;
	}

	public void onCountryChange() {
		emissions = (countryId == null) ? null : service.findEmissions(countryId);
	}

	public void save() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (countryId == null || year == null || co2 == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte alle Felder ausfüllen", null));
			return;
		}
		boolean created = service.saveOrUpdate(countryId, year, co2);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				created ? "Neuer Wert gespeichert" : "Bestehender Wert korrigiert", null));
		onCountryChange();
	}

	public Long getCountryId() { return countryId; }
	public void setCountryId(Long countryId) { this.countryId = countryId; }
	public Integer getYear() { return year; }
	public void setYear(Integer year) { this.year = year; }
	public Double getCo2() { return co2; }
	public void setCo2(Double co2) { this.co2 = co2; }
	public List<Emission> getEmissions() { return emissions; }
}