package it.amicosmanettone.assistenza.assistenzaitalia.chat;

public class ChatXmlBean {

	private String message;
	private String user;
	private String date;
	private String imagePath;
	private String rango;
	private String id;
	
	public ChatXmlBean(String message, String user, String date, String imagePath, String rango, String id) {
	
		this.message=message;
		this.user=user;
		this.date=date;
		this.imagePath=imagePath;
		this.rango=rango;
		this.id=id;
		
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getRango() {
		return rango;
	}


	public void setRango(String rango) {
		this.rango = rango;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
