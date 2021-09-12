/*
 * 2021-09-12
 * Jason Li
 */
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Connected {
	
	private static final String DELIMITER = ", ";
	private static Map<Integer, List<String>> networkMap = new HashMap<Integer, List<String>>();
	private static Map<String, Integer> citiesNodes = new HashMap<String, Integer>();
	
	public static void main(String[] args)  {

		final String file_name = args[0];
		final String first_city = args[1].toLowerCase();
		final String second_city = args[2].toLowerCase();
		
		try (Stream<String> lines = Files.lines(Paths.get(file_name), Charset.defaultCharset())) {
			  lines.forEachOrdered(line -> {
				  String[] citiesConnected = parseLine(line);
				  addCitiesToMap(citiesConnected[0],citiesConnected[1]);
			  });
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (checkConnected(first_city, second_city)) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
	}
	
	private static boolean checkConnected(String cityFirst, String citySecond) {
		Integer networkFirst = citiesNodes.get(cityFirst);
		Integer networkSecond = citiesNodes.get(citySecond);
		if ( networkFirst == null || networkSecond == null) {
			return false;
		} else if ( networkFirst != networkSecond ) {
			return false;
		} else {
			return true;
		}
	}
	
	private static void addNetworkCity(List<String> listOne, List<String> listTwo) {
		List<String> listTwoCopy = listTwo;
        listTwoCopy.removeAll(listOne);
        listOne.addAll(listTwoCopy);
	}
	
	private static void addCitiesToMap(String cityFirst, String citySecond) {
		Integer networkFirst = citiesNodes.get(cityFirst);
		Integer networkSecond = citiesNodes.get(citySecond);
		
		if (networkFirst != null) {
			if (networkSecond!= null) {
				if (networkFirst == networkSecond) {
					return;
				}
				
				if (networkMap.get(networkFirst).size() < networkMap.get(networkSecond).size()) {
					networkMap.get(networkFirst).forEach(city -> {
						citiesNodes.put(city, networkSecond);
					});
					addNetworkCity(networkMap.get(networkSecond), networkMap.get(networkFirst));
				} else {
					networkMap.get(networkSecond).forEach(city -> {
						citiesNodes.put(city, networkFirst);
					});
					addNetworkCity(networkMap.get(networkFirst), networkMap.get(networkSecond));
				}
				
			} else {
				citiesNodes.put(citySecond, networkFirst);
				networkMap.get(networkFirst).add(citySecond);
				
			}
			
		} else if (networkSecond != null){
			citiesNodes.put(cityFirst, networkSecond);
			networkMap.get(networkSecond).add(cityFirst);
			
		} else {
			Integer networkSize = networkMap.size();
			citiesNodes.put(cityFirst, networkSize);	
			citiesNodes.put(citySecond, networkSize);
			networkMap.put(networkSize, new ArrayList<String>(Arrays.asList(cityFirst, citySecond)));
			
		}
		
	}
	
	private static String[] parseLine(String line) {
		return line.trim().toLowerCase().split(DELIMITER);
	}

}
