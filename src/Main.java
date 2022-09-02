import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		// String ArquivoExistente = new
		// File("C:/Users/Pichau/eclipse-workspace/LerArquivo/teste.txt");
		
		try {
			File file = new File("C:/Users/Pichau/eclipse-workspace/LerArquivo/logLu.log"); // java.io.File
			FileReader fr = new FileReader(file); // java.io.FileReader
			BufferedReader br = new BufferedReader(fr); // java.io.BufferedReader
			String line;

			File output = new File("C:/Users/Pichau/eclipse-workspace/LerArquivo/arquivo.txt");
			FileWriter writer = new FileWriter(output);
			BufferedWriter buff = new BufferedWriter(writer);

			Scanner sc = new Scanner(System.in);
			System.out.println("PADRÂO DE FORMATAÇÂO - Exemplo (25-09-2001 12:32)");
			
			System.out.println("");
			
			System.out.println("Digite a data de início: ");
			String dataInicio = sc.nextLine();

			System.out.println("Digite a data final: ");
			String dataFinal = sc.nextLine();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			DateTimeFormatter formatterLog = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss 'BRT' yyyy", Locale.ENGLISH).withZone(ZoneId.systemDefault());

			LocalDateTime date1 = LocalDateTime.parse(dataInicio, formatter); // string para data
			LocalDateTime date2 = LocalDateTime.parse(dataFinal, formatter);
			
			Long dInicial = date1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // datas em milisegundos do tipo long (para fazer a comparação na linha 67, se fosse localDateTime, não daria)
			Long dFinal = date2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

			while ((line = br.readLine()) != null) { // lê todas as linhas
				TemporalAccessor lineDate = TryParseDate(line, formatterLog);
				if (lineDate != null) {
					Instant instant = Instant.from(lineDate);
					Long lineTime = Instant.EPOCH.until(instant, ChronoUnit.MILLIS); //pega os milisegundos do Long para fazer a comparação...

					if (dInicial <= lineTime && dFinal >= lineTime) {   
						System.out.println(line);

						buff.write(line);
						buff.newLine();
					}
				} 
			}
			buff.flush();
			buff.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static TemporalAccessor TryParseDate(String conteudoLinha, DateTimeFormatter date) {
		try {
			return date.parse(conteudoLinha.substring(4, 28)); // converter para um objeto de data
		} catch (DateTimeParseException ex) {
			return null;
		}
	}
}