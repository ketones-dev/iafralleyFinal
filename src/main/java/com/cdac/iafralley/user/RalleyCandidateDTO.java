package com.cdac.iafralley.user;





import com.cdac.iafralley.user.validation.ValidEmail;

public class RalleyCandidateDTO {

	

	

	@ValidEmail
	private String emailid;

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	

	

}
