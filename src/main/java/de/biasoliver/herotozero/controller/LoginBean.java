package de.biasoliver.herotozero.controller;

import de.biasoliver.herotozero.model.User;
import de.biasoliver.herotozero.service.UserService;
import de.biasoliver.herotozero.util.PasswordUtil;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named("loginBean")
@ViewScoped
public class LoginBean implements Serializable {

	private final UserService userService = new UserService();
	private String username;
	private String password;

	public String login() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		User user = userService.findByUsername(username);
		if (user != null && PasswordUtil.verify(password, user.getPasswordHash())) {
			HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
			request.changeSessionId();
			ctx.getExternalContext().getSessionMap().put("user", user.getUsername());
			return "/admin/admin.xhtml?faces-redirect=true";
		}
		ctx.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Benutzername oder Passwort falsch", null));
		return null;
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index.xhtml?faces-redirect=true";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}