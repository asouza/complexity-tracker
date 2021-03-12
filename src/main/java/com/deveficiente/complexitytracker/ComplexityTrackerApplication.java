package com.deveficiente.complexitytracker;

import java.io.FileReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.deveficiente.complexitytracker.generatehistory.GenerateHistoryController;
import com.deveficiente.complexitytracker.generatehistory.GenerateRemoteHistoryRequest;

@SpringBootApplication
public class ComplexityTrackerApplication implements CommandLineRunner {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ComplexityTrackerApplication.class, args);
	}

	@Autowired
	private GenerateHistoryController historyController;

	@Override
	public void run(String... args) throws Exception {
		Reader in = new FileReader("responses.csv");
		Iterator<CSVRecord> records = CSVFormat.RFC4180.parse(in).iterator();
		// skipando header
		records.next();
		Set<String> cdd = Set.of(
				StringUtils.trimAllWhitespace("sandro.santos@zup.com.br, fabio.vilela@zup.com.br, artur.veiga@zup.com.br, abraao.queiroz@zup.com.br, renato.souza@zup.com.br, matheus.ferreira@zup.com.br, erico.monteiro@zup.com.br, pedro.coelho@zup.com.br, rodrigo.valentim@zup.com.br, guilherme.campos@zup.com.br, everton.silva@zup.com.br, roniceli.moura@zup.com.br, valdir.borges@zup.com.br, otavio.jesus@zup.com.br, marcelo.amorim@zup.com.br, gabriel.otsuka@zup.com.br, renato.martins@zup.com.br, thiago.peixoto@zup.com.br, wesley.santos@zup.com.br, pedro.bruno@zup.com.br, marcos.barra@zup.com.br, wesley.souza@zup.com.br, caio.melo@zup.com.br, fellipe.gurgel@zup.com.br, luis.maximo@zup.com.br, leticia.gondo@zup.com.br, lucas.santos3@zup.com.br, diego.alexandro@zup.com.br, antonio.capra@zup.com.br, alexssander.fonseca@zup.com.br, ribeiro.lucas@zup.com.br, carolina.fonseca@zup.com.br, rodrigo.lima@zup.com.br, idnilson.junior@zup.com.br, elias.lima@zup.com.br, vinicius.guardieiro@zup.com.br, elias.coelho@zup.com.br, elias.borges@zup.com.br")
						.split(","));
		
		Set<String> naoCdd = Set.of(StringUtils.trimAllWhitespace("levi.dias@zup.com.br, silas.silva@zup.com.br, cristian.abreu@zup.com.br, leandro.marques@zup.com.br, breno.pereira@zup.com.br, wilton.ferreira@zup.com.br, gabriel.paula@zup.com.br, edson.junior@zup.com.br, bruno.barros@zup.com.br, wesly.borges@zup.com.br, bruno.santiago@zup.com.br, joao.oliveira2@zup.com.br, alefh.silva@zup.com.br, tereza.niederauer@zup.com.br, diego.cavalcanti@zup.com.br, miguel.aleman@zup.com.br, yan.diniz@zup.com.br, sidartha.carvalho@zup.com.br, valeria.morais@zup.com.br, diego.francisco@zup.com.br, gabriel.oliveira2@zup.com.br, miriam.luz@zup.com.br, fabricio.guimaraes@zup.com.br, bleno.claus@zup.com.br, claudineia.fernandes@zup.com.br, lincoln.araujo@zup.com.br, allan.sklarow@zup.com.br, daniel.medeiros@zup.com.br, rodrigo.alexandre@zup.com.br, guilherme.ruella@zup.com.br, gabriel.morato@zup.com.br, felipe.possari@zup.com.br, luiz.carvalho@zup.com.br, walter.oliveira@zup.com.br, matheus.cruz@zup.com.br, samuel.araujo@zup.com.br, anderson.cancio@zup.com.br, dayana.souza@zup.com.br").split(",")); 

			while (records.hasNext()) {
				try {
					CSVRecord record = records.next();
					String subjectId = record.get(3);
					String email = record.get(2).trim();
					if(cdd.contains(email)) {
						subjectId = "cdd-".concat(subjectId);
					}
					if(naoCdd.contains(email)) {
						subjectId = "sem-".concat(subjectId);
					}
					if(!cdd.contains(email) && !naoCdd.contains(email)) {
						subjectId = "naoclassificado-".concat(subjectId);					
					}
					
					
					String repoUrl = record.get(6);
					System.out.println(String.format("Importando id %s da url %s",
							subjectId, repoUrl));
	
					Map<String, String> params = Map.of("projectId", subjectId,
							"url", repoUrl, "javaFilesFolderPath", "src/main/java",
							"startDate", "2021-03-06", "endDate", "2021-03-07");
	
					ResponseEntity<String> retorno = new RestTemplate()
							.postForEntity(
									"http://localhost:8080/generate-history/remote",
									params, String.class);
					System.out.println(retorno.getBody());
				} catch (RestClientException e) {
					System.err.print(e.getMessage());
				}
			}
	}
}
