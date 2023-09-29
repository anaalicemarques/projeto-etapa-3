package br.com.etapa3;

public class Colunista {

	private String nome;
	private String link;
	
	public Colunista() {
	}
	
	public Colunista(String nome, String link) {
		super();
		this.nome = nome;
		this.link = link;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
