package competitivenessinexport;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GraphPanel2 extends JPanel {

	public static final int width = 800;
	public static final int heigth = 400;
	public static final int padding = 25;
	public static final int labelPadding = 25;
	private int pointWidth = 4;
	private int numberYDivisions = 10;
	private Color gridColor = new Color(200, 200, 200, 200);
	static List<String> csvData = new ArrayList<>(); // x좌표를 찍는 함수와 TSI지수 찍는 함수에 사용되기 때문에 전역 선언

	private List<LineData> lines;
	LineData line;

	public GraphPanel2(List<LineData> lines) {
		this.lines = lines;
		line = lines.get(0);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw white background
		g2.setColor(Color.WHITE);
		g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
				getHeight() - 2 * padding - labelPadding);
		g2.setColor(Color.BLACK);

		// create hatch marks and grid lines for y axis.
		for (int index = 0; index < numberYDivisions + 1; index++) {
			int x0 = padding + labelPadding;
			int x1 = pointWidth + padding + labelPadding;
			int y0 = getHeight() - ((index * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding
					+ labelPadding);
			int y1 = y0;

			if (line.scores.size() > 0) {
				g2.setColor(gridColor);
				g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
				g2.setColor(Color.BLACK);
				String yLabel = ((int) ((-1 + (1 - (-1)) * ((index * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
			}
			g2.drawLine(x0, y0, x1, y1);
		}

		// and for x axis, x 축은 CSV file 인덱스 0번째 배열을 뿌려서 기간을 String으로 찍어준다.
		// (arrDateOfData)
		String[] arrDateOfData = null;
		for (int index = 0; index < line.scores.size(); index++) {
			arrDateOfData = csvData.get(index).split(",");
			if (line.scores.size() > 1) {
				int x0 = index * (getWidth() - padding * 2 - labelPadding) / (line.scores.size() - 1) + padding
						+ labelPadding;
				int x1 = x0;
				int y0 = getHeight() - padding - labelPadding;
				int y1 = y0 - pointWidth;
				if ((index % ((int) ((line.scores.size() / 20.0)) + 1)) == 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
					g2.setColor(Color.BLACK);
					String xLabel = arrDateOfData[0] + ""; // x 축 좌표에 데이터 기간으로 찍기
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
				}
				g2.drawLine(x0, y0, x1, y1);
			}
		}

		// create x and y axis
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding,
				getHeight() - padding - labelPadding);

		for (int index = 0; index < lines.size(); index++) {
			LineData line = lines.get(index);

			line.draw(g2, getWidth(), getHeight(), 1, -1);
		}
	}

	// 무역특화지수(Trade Specialization Index)와 감정지수(Emotion Index) 각각의 CSV file 읽어서 그래프 위에 뿌리는 함수
	public static void createAndShowGui() {
		// list 선언 및 초기화, scores를 받아 객체 생성 및 인스턴스를 생성하여 객체에 연결
		List<String> emotionIndexCsvData = new ArrayList<>();
		List<Double> tradeSpecializationIndex = new ArrayList<>();
		List<Double> tradeSpecializationIndex2 = new ArrayList<>();
		List<Double> tradeSpecializationIndex3 = new ArrayList<>();
		List<Double> emotionIndex = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:\\news\\ImExport.csv"));
			BufferedReader br2 = new BufferedReader(new FileReader("C:\\news\\rateMean.csv"));
			String lineOfTradeSpecializationIndex;
			while ((lineOfTradeSpecializationIndex = br.readLine()) != null) {
				csvData.add(lineOfTradeSpecializationIndex);
			}
			for (int index = 0; index < csvData.size(); index++) {
				String[] tsiData = csvData.get(index).split(",");
				tradeSpecializationIndex.add(Double.parseDouble(tsiData[1]));
				tradeSpecializationIndex2.add(Double.parseDouble(tsiData[2]));
				tradeSpecializationIndex3.add(Double.parseDouble(tsiData[3]));
			}
			
			String lineOfEmotionIndex;
			while ((lineOfEmotionIndex = br2.readLine()) != null) {
				emotionIndexCsvData.add(lineOfEmotionIndex);
			}
			for (int index = 0; index < emotionIndexCsvData.size(); index++) {
				String[] eiData = emotionIndexCsvData.get(index).split(",");
				emotionIndex.add(Double.parseDouble(eiData[3]));
			}
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 그래프 위에 뿌리기
		JFrame frame = new JFrame("Draw TSI & EI Graph");
		List<LineData> lines = new ArrayList<>();
		lines.add(new LineData(tradeSpecializationIndex)); // 무역특화지수 그래프에 뿌리기
		lines.add(new LineData(emotionIndex, Color.red, Color.red)); // 뉴스기사 크롤링 감정지수 그래프에 뿌리기
		lines.add(new LineData(tradeSpecializationIndex2, Color.green, Color.green));
		lines.add(new LineData(tradeSpecializationIndex3, Color.yellow, Color.yellow));
		GraphPanel2 mainPanel = new GraphPanel2(lines);
		frame.getContentPane().add(mainPanel);
		mainPanel.setPreferredSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
}
