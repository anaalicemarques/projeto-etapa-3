package br.com.etapa3;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.awt.Container;
import java.awt.FlowLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Aplicacao extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
		Aplicacao ex = new Aplicacao();
		ex.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ex.setVisible(true);
		ex.setTitle("USANDO INSERT");
		ex.setSize(400,200);
	}

	public Aplicacao() throws SQLException, ClassNotFoundException, IOException {
	
		CodecRegistry pojoCodecResgistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		MongoClient mongoClient = new MongoClient("localhost:27017",
				com.mongodb.MongoClientOptions.builder().codecRegistry(pojoCodecResgistry).build());

		MongoDatabase database = mongoClient.getDatabase("projeto-etapa-3").withCodecRegistry(pojoCodecResgistry);
		MongoCollection<Colunista> collection = database.getCollection("oeste-colunistas", Colunista.class);

		List<String> listaLinks = extrairDadosLinks();
		
		String[] dados;
		String nome = new String();
		
		for (String link : listaLinks) {
			dados = link.split("/");
			
			if (dados.length == 5) {
				nome = dados[4];				
			} else {
				nome = null;
			}
			collection.insertOne(new Colunista(nome, link));
		}
		
		System.out.println();
		System.out.println("Inserção realizada!");
		
		mongoClient.close();

		Container P = getContentPane();
		P.setLayout(new FlowLayout());
		JLabel mensagem = new JLabel("Você acabou de inserir os dados na tabela!");
		P.add(mensagem);
	}
	
	public List<String> extrairDadosLinks() throws IOException {
		String url = "https://revistaoeste.com";
		String urlColunista = url + "/colunista";
		List<String> listaLinks = new ArrayList<>();

		Document doc = Jsoup.connect(url).get();

		Elements elements = doc.select("a");
		
		for (Element element : elements) {
			String link = element.attr("href");

			if (!link.isEmpty()) {
				if(link.contains(urlColunista)) {
					listaLinks.add(link);
				}
			}
		}
		
		return listaLinks;
	}
	
}
