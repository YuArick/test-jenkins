package com.example.testJenkins;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.testJenkins.entity.StockData;
import com.example.testJenkins.entity.StockList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

@Controller
public class Calculate {
	@RequestMapping("/")
	public String calculate() {
		return "index";
	}

	@RequestMapping("/method1")
	@ResponseBody

	public String method1(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) + Integer.parseInt((String) params.get("b")) + "";
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		byte[] test = "1231111111111111111111111111111111111111111111111111111111111111111".getBytes();
		System.out.println(test.length + "==============");
		byteArray.write(test, 0, 30);

		return ans;
	}

	@RequestMapping("/method2")
	@ResponseBody
	public String method2(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) - Integer.parseInt((String) params.get("b")) + "";
		return ans;
	}

	@RequestMapping("/method3")
	@ResponseBody
	public String method3(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) * Integer.parseInt((String) params.get("b")) + "";
		return ans;
	}

	@RequestMapping("/method4")
	@ResponseBody
	public String method4(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) / Integer.parseInt((String) params.get("b")) + "";
		return ans;
	}

	@RequestMapping("/method5")
	@ResponseBody
	public String method5(@RequestBody Map<String, Object> params) {
		String stockCode = Integer.parseInt((String) params.get("stock"))+".TW";
//		String stockCode = "AAPL"; // 股票代碼
		LocalDateTime now =LocalDateTime.now();
		// 取得當前時間

	    // 取得一個月前的時間
	    LocalDateTime oldtime = now.minusMonths(60);
//		LocalDateTime fiveYearsAgo = now.minus(5, ChronoUnit.YEARS);
//		System.out.println(fiveYearsAgo);
	    // 轉換成時間戳記
	    long start = oldtime.atZone(ZoneId.systemDefault()).toEpochSecond();
	    System.out.println(start);
	    long end = now.atZone(ZoneId.systemDefault()).toEpochSecond();
	    
		String urlStr = "https://finance.yahoo.com/quote/" + stockCode + "?p=" + stockCode;
		String apiStr = "https://query1.finance.yahoo.com/v8/finance/chart/" + stockCode
//				+ "?period1=1599926400&period2=1608566400&interval=1d";
		+ "?period1="+start+"&period2="+end+"&interval=1d";
		String jsonString = "";
		String timestamp = "";

		try {
			URL url = new URL(apiStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				jsonString += inputLine;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		List<String> time = new ArrayList<String>();
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(jsonString, JsonObject.class);
		JsonArray jsonArray = json.getAsJsonObject("chart").getAsJsonArray("result").get(0).getAsJsonObject()
				.getAsJsonObject("indicators").getAsJsonArray("quote");
		JsonArray timestamplist = json.getAsJsonObject("chart").getAsJsonArray("result").get(0).getAsJsonObject()
				.getAsJsonArray("timestamp");
//		for(int i = 0; i < timestamplist.size(); i++) {
//			time.add(timestamplist.get(i).getAsString());
//		}
		List<StockData> data = new ArrayList<StockData>();
		JsonObject obj = jsonArray.get(0).getAsJsonObject();
		JsonArray openlist = obj.get("open").getAsJsonArray();
		JsonArray highlist = obj.get("high").getAsJsonArray();
		JsonArray lowlist = obj.get("low").getAsJsonArray();
		JsonArray closelist = obj.get("close").getAsJsonArray();
		JsonArray volumelist = obj.get("volume").getAsJsonArray();
		for (int i = 0; i < timestamplist.size(); i++) {
			StockData sd = new StockData();
			sd.setOpen(openlist.get(i).getAsBigDecimal().setScale(2, RoundingMode.HALF_UP));
			sd.setOpen(openlist.get(i).getAsBigDecimal().setScale(2, RoundingMode.HALF_UP));
			sd.setHigh(highlist.get(i).getAsBigDecimal().setScale(2, RoundingMode.HALF_UP));
			sd.setLow(lowlist.get(i).getAsBigDecimal().setScale(2, RoundingMode.HALF_UP));
			sd.setVolume(volumelist.get(i).getAsLong());
			sd.setClose(closelist.get(i).getAsBigDecimal());
			sd.setTimestamp(timestamplist.get(i).getAsLong() * 1000);
			// 儲存數據到CSV文件
			data.add(sd);
		}
		saveToCSV(data, "TEST");
		return gson.toJson(data);
	}

	@RequestMapping("/method7")
	@ResponseBody
	private void method7(@RequestBody Map<String, Object> params) {
		feachStockList("2",false);
		feachStockList("4",true);
	}
	@RequestMapping("/method8")
	@ResponseBody
	private String method8(@RequestBody Map<String, Object> params) {
		return getStock();
	}

	private void feachStockList(String model,Boolean override) {
		ArrayList<StockList> stockList = new ArrayList<StockList>();
		try {

			String url = "https://isin.twse.com.tw/isin/C_public.jsp?strMode="+ model;
			Document doc;
			doc = Jsoup.connect(url).get();
			Element table = doc.select("table").get(1);
			Elements rows = table.select("tr");

			for (int i = 2; i < rows.size(); i++) {
				Element row = rows.get(i);
				Elements cols = row.select("td");
				if(cols.size()>5) {
					String CFICode = cols.get(5).text().trim();
					if("ESVUFR".equals(CFICode)) {
						String code = cols.get(0).text().trim().split("　")[0];
						String name = cols.get(0).text().trim().split("　")[1];
						String category = cols.get(3).text().trim();
						String industry = cols.get(4).text().trim();
						stockList.add(new StockList(code, name, category, industry , CFICode));
					}
				}
			}
			saveStock(stockList, "",override);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/method6")
	@ResponseBody
	public String method6(@RequestBody Map<String, Object> params) {
		List<StockData> stockDataList = new ArrayList<>();
		String csvFile = "stock_data.csv";
		String json = "";
		// 假設CSV檔案中第一行為標題，第二行開始才是資料
		int skipLines = 1;

		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

			String[] headerRow = reader.readNext(); // 讀取標題列，忽略
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {

				StockData stockData = new StockData();

				// 將字串轉成對應的型態
				stockData.setTimestamp(Long.parseLong(nextLine[0]));
				stockData.setOpen(BigDecimal.valueOf(Double.parseDouble(nextLine[1])));
				stockData.setHigh(BigDecimal.valueOf(Double.parseDouble(nextLine[2])));
				stockData.setLow(BigDecimal.valueOf(Double.parseDouble(nextLine[3])));
				stockData.setClose(BigDecimal.valueOf(Double.parseDouble(nextLine[4])));
				stockData.setVolume(Long.parseLong(nextLine[5]));

				stockDataList.add(stockData);
			}

			Gson gson = new Gson();
			json = gson.toJson(stockDataList);
			System.out.println(json);
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	private static void saveToCSV(List<StockData> data, String fileName) {
		String[] header = { "Timestamp", "Open", "High", "Low", "Close", "Volume" };
		try (CSVWriter writer = new CSVWriter(new FileWriter("stock_data.csv"))) {
//		try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))){
			writer.writeNext(header);
			// 寫入資料
			for (StockData sd : data) {
				String[] rowData = { sd.getTimestamp() + "", String.valueOf(sd.getOpen()), String.valueOf(sd.getHigh()),
						String.valueOf(sd.getLow()), String.valueOf(sd.getClose()), String.valueOf(sd.getVolume()) };
				writer.writeNext(rowData);
			}
			writer.flush();
			System.out.println("CSV file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveStock(List<StockList> data, String fileName,Boolean override ) {
		String[] header = { "Code", "Name", "Industry", "Category" ,"CFIcode" };
		try (CSVWriter writer = new CSVWriter(new FileWriter("stock_List.csv",override))) {
//		try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))){
			writer.writeNext(header);
			// 寫入資料
			for (StockList stock : data) {
				String[] rowData = { stock.getCode(), stock.getName(), stock.getIndustry(), stock.getCategory() , stock.getCFICode()};
				writer.writeNext(rowData);
			}
			writer.flush();
			System.out.println("CSV file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String getStock() {
		List<StockList> stockDataList = new ArrayList<>();
		String csvFile = "stock_List.csv";
		String json = "";
		// 假設CSV檔案中第一行為標題，第二行開始才是資料
		int skipLines = 1;

		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

			String[] headerRow = reader.readNext(); // 讀取標題列，忽略
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {

				StockList stockData = new StockList(nextLine[0],nextLine[1],nextLine[2],nextLine[3],nextLine[4]);

				stockDataList.add(stockData);
			}

			Gson gson = new Gson();
			json = gson.toJson(stockDataList);
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
