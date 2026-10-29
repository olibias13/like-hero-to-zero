package de.biasoliver.herotozero.controller;

import java.io.Serializable;
import java.util.List;

import de.biasoliver.herotozero.model.Country;
import de.biasoliver.herotozero.model.Emission;
import de.biasoliver.herotozero.service.EmissionService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("countryBean")
@ViewScoped
public class CountryBean implements Serializable {

	private EmissionService service = new EmissionService();
	private List<Country> countries;
	private Long selectedId;
	private Emission result;

	public List<Country> getCountries() {
		if (countries == null) {
			countries = service.findAllCountries();
		}
		return countries;
	}

	public void onCountryChange() {
		result = (selectedId == null) ? null : service.findLatestEmission(selectedId);
	}

	public Long getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}

	public Emission getResult() {
		return result;
	}
}