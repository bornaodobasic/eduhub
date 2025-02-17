
package fer.progi.backend.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fer.progi.backend.domain.Djelatnik;
import fer.progi.backend.dto.ZahtjeviDTO;
import fer.progi.backend.service.DjelatnikService;
import fer.progi.backend.service.impl.S3Service;



@RestController
@RequestMapping("/api/djelatnik")
@PreAuthorize("hasAuthority('Djelatnik')")
public class DjelatnikController {

	@Autowired
	private DjelatnikService DjelatnikService;
	private String csvFilePath;


	@Autowired
	private S3Service s3Service;

	@GetMapping("")
	public List<Djelatnik> listDjelatnik() {
		return DjelatnikService.listAll();
	}

	@PostMapping("/add")
	@Secured("ROLE_administrator")
	public Djelatnik dodajDjelatnik(@RequestBody Djelatnik djelatnik) {
		return DjelatnikService.dodajDjelatnik(djelatnik);
	}

	@GetMapping("/pogledajIzdanePotvrde")
	public List<ZahtjeviDTO> pregledIzdanihPotvrda() throws ParseException {
		List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String csvFileKey = "zahtjevi/zahtjevinew.csv"; // Ključ datoteke na S3

		Path tempFilePath = null;

		try {

			tempFilePath = Files.createTempFile("zahtjevi", ".csv");


			byte[] csvContent = s3Service.getFile(csvFileKey);
			Files.write(tempFilePath, csvContent);


			try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
				String line = reader.readLine();
				if (line == null) return listaZahtjeva;

				while ((line = reader.readLine()) != null) {
					String[] row = line.split(",");
					if (row.length == 4) {
						listaZahtjeva.add(new ZahtjeviDTO(
								row[0],
								row[1],
								row[2],
								dateFormat.parse(row[3].trim())
						));
					} else {
						System.err.println("Invalid row format: " + line);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error handling CSV file: " + e.getMessage());
		} finally {

			if (tempFilePath != null) {
				try {
					Files.delete(tempFilePath);
				} catch (IOException e) {
					System.err.println("Error deleting temporary file: " + e.getMessage());
				}
			}
		}

		return listaZahtjeva;
	}


	@GetMapping("/pogledajIzdanePotvrdeImePrezime")
	public List<ZahtjeviDTO> pregledIzdanihPotvrdaImePrezime(@RequestParam String email) throws ParseException {
		List<ZahtjeviDTO> listaZahtjeva = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String csvFileKey = "zahtjevi/zahtjevinew.csv";

		Path tempFilePath = null;

		try {

			tempFilePath = Files.createTempFile("temp-zahtjevi", ".csv");


			byte[] csvContent = s3Service.getFile(csvFileKey);
			Files.write(tempFilePath, csvContent);


			try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
				String line = reader.readLine();
				if (line == null) return listaZahtjeva;

				while ((line = reader.readLine()) != null) {
					String[] row = line.split(",");
					if (row[2].equals(email)) {
						listaZahtjeva.add(new ZahtjeviDTO(
								row[0],
								row[1],
								row[2],
								dateFormat.parse(row[3].trim())
						));
					} else if (row.length != 3) {
						System.err.println("Invalid row format: " + line);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error handling CSV file: " + e.getMessage());
		} finally {

			if (tempFilePath != null) {
				try {
					Files.delete(tempFilePath);
				} catch (IOException e) {
					System.err.println("Error deleting temporary file: " + e.getMessage());
				}
			}
		}

		return listaZahtjeva;
	}
}