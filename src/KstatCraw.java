package chromeserch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class KstatCraw {

	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
	public static final String WEB_DRIVER_PATH = "/Users/bradkim/crawling/Drivers/chromedriver"; // 드라이버 경로
	public static String passWD = "new1212!";
	public static ArrayList<String> arr = new ArrayList<String>();
	public static String dataList = "";
	public static int year = 2018;	
	public static int month = 1;
	public static DecimalFormat df = new DecimalFormat("00");
	public static DecimalFormat tradeValue = new DecimalFormat("#.###");

	public static void main(String[] args) {
		// 드라이버 설정
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 크롬 설정을 담은 객체 생성
		ChromeOptions options = new ChromeOptions();
		// 브라우저가 눈에 보이지 않고 내부적으로 돈다.
		// 설정하지 않을 시 실제 크롬 창이 생성되고, 어떤 순서로 진행되는지 확인할 수 있다.
		options.addArguments("headless");

		// 위에서 설정한 옵션은 담은 드라이버 객체 생성
		// 옵션을 설정하지 않았을 때에는 생략 가능하다.
		// WebDriver객체가 곧 하나의 브라우저 창이라 생각한다.
		WebDriver driver = new ChromeDriver(/* options */);

		// 이동을 원하는 url
		String url = "https://stat.kita.net/newMain.screen";

		// WebDriver을 해당 url로 이동한다.
		driver.get(url);

		// 브라우저 이동시 생기는 로드시간을 기다린다.
		// HTTP응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 1초를 대기한다.
		try {
			Thread.sleep((int) (Math.random() * 5000));
		} catch (InterruptedException e) {
		}
		// 로그인
		driver.findElement(By.xpath("//*[@id=\"header\"]/div/div[2]/ul/li[2]/a")).click();
		try {
			Thread.sleep((int) (Math.random() * 5000));
		} catch (InterruptedException e) {
		}
		driver.findElement(By.xpath("//*[@id=\"userId\"]")).sendKeys("httpksynet");
		try {
			Thread.sleep((int) (Math.random() * 5000));
		} catch (InterruptedException e) {
		}
		driver.findElement(By.xpath("//*[@id=\"pwd\"]")).sendKeys(passWD);
		try {
			Thread.sleep((int) (Math.random() * 5000));
		} catch (InterruptedException e) {
		}
		driver.findElement(By.xpath("//*[@id=\"loginBtn\"]"))
				.click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		// 검색
		driver.findElement(By.xpath("//*[@id=\"gnb\"]/ul/li[1]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"gnb\"]/ul/li[1]/div/div/ul/li[1]/ul/li[3]/a")).click();
		// 화장품 코드 입
		driver.findElement(By.xpath("//*[@id=\"s_cond_unit_num\"]")).sendKeys("3304");
		// 교역 선택
		driver.findElement(By.xpath("//*[@id=\"contents\"]/div[2]/form/fieldset/div[1]/div[2]/select/option[2]")).click();
		for (int i = 5; i >= 1; i--) {
			driver.findElement(By.xpath("//*[@id=\"contents\"]/div[2]/form/fieldset/div[2]/div[1]/select/option["+i+"]")).click();
			for (int j = 1; j <=12; j++) {
				if (i == 1) {
					driver.findElement(By.xpath("//*[@id=\"contents\"]/div[2]/form/fieldset/div[2]/div[2]/select/option["+j+"]")).click();
					if (j == 3) {
						String rawExportData = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[2]/td[11]")).getText().replace(",", "");
						String rawImportData = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[2]/td[13]")).getText().replace(",", "");
						String rawExportEye = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[4]/td[11]")).getText().replace(",", "");
						String rawImportEye = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[4]/td[13]")).getText().replace(",", "");
						String rawExportLip = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[5]/td[11]")).getText().replace(",", "");
						String rawImportLip = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[5]/td[13]")).getText().replace(",", "");
						String exportData = rawExportData.replace(",", "");
						String importData = rawImportData.replace(",", "");
						dataList = Integer.toString(year) + df.format(month)+ "," + calculate_value (rawExportData, rawImportData)+ "," + calculate_value (rawExportEye, rawImportEye)+ "," + calculate_value (rawExportLip, rawImportLip);
					    arr.add(dataList);
						break;
					}
				} else {
					driver.findElement(By.xpath("//*[@id=\"contents\"]/div[2]/form/fieldset/div[2]/div[2]/select/option["+j+"]")).click();					
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				driver.findElement(By.xpath("//*[@id=\"contents\"]/div[2]/form/fieldset/div[3]/a/img")).click();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				String rawExportData = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[2]/td[11]")).getText().replace(",", "");
				String rawImportData = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[2]/td[13]")).getText().replace(",", "");
				String rawExportEye = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[4]/td[11]")).getText().replace(",", "");
				String rawImportEye = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[4]/td[13]")).getText().replace(",", "");
				String rawExportLip = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[5]/td[11]")).getText().replace(",", "");
				String rawImportLip = driver.findElement(By.xpath("//*[@id=\"mySheet1\"]/tbody/tr[3]/td/div/div[1]/table/tbody/tr[5]/td[13]")).getText().replace(",", "");
				String exportData = rawExportData.replace(",", "");
				String importData = rawImportData.replace(",", "");
				dataList = Integer.toString(year) + df.format(month)+ "," + calculate_value (rawExportData, rawImportData)+ "," + calculate_value (rawExportEye, rawImportEye)+ "," + calculate_value (rawExportLip, rawImportLip);
				arr.add(dataList);
				month++;
				if (month >12) {
					month = 1;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			year++;
		}
		for (int i = 0; i< arr.size(); i++) {
			System.out.println(arr.get(i));
			make_CSV_raw(arr.get(i));
		}

		try {
			// 드라이버가 null이 아니라면
			if (driver != null) {
//			            // 드라이버 연결 종료
//			            driver.close(); // 드라이버 연결 해제
				//
//			            // 프로세스 종료
//			            driver.quit();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	static void make_CSV_raw(String data) { //CSV 생성
		File csv = new File("/Users/bradkim/Desktop/ImExport.csv");
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(csv, true));
			bw.write(data);
			bw.newLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.flush();
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static String calculate_value (String exportV, String importV) { //무역 특화지수 계산
		double exvalue = Double.parseDouble(exportV);
		double imvalue = Double.parseDouble(importV);
		double calculate = (exvalue - imvalue) / (exvalue + imvalue);
		String result = tradeValue.format(calculate);
		return result;
	} 
	
}