package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryByAPI {

	static String api = "http://obelix.epd.gov.hk:5050/tide/oneWeek?stationId=";

	// to get nearest 7 days's tidal information
	// http://obelix.epd.gov.hk:5050/tide/oneWeek
	public static void main(String[] args) {
		// 6: Tai O
		callAPI(6);

	}

	private static void callAPI(int stationId) {

		try {
			URL url = new URL(api + stationId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			// return 200 for OK
			int status = con.getResponseCode();
//			System.out.println(status);

			// reading response content
			String content = FullResponseBuilder.getFullResponse(con);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(content);

			System.out.println("data.addRows([");
			for (JsonNode jn : node) {
				String dt = jn.get("datetime").textValue();
				String h = jn.get("height").asText();
				System.out.println("[new Date('" + dt + "'), " + h + "],");
			}
			System.out.println("]);");

			con.disconnect();

//			json2Chart(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	private static void json2Chart(String content) {
//		JsonNode root = null;
//		String json = JsonRenderer.renderDataTable(data, true, false).toString();
//
//		        try{
//		            JsonParser parser = new JsonFactory().createJsonParser(json)
//		                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
//		                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
//		                 root = new ObjectMapper().readTree(parser);
//		            }catch(Exception e){
//		              e.printStackTrace();
//		            }
//		
//	}

}

class FullResponseBuilder {

	public static String getFullResponse(HttpURLConnection con) {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}