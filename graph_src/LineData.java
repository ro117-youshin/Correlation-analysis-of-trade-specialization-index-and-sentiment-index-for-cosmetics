package competitivenessinexport;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;

public class LineData {
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private int pointWidth = 4;
	public List<Double> scores;
	public Color lineColor = new Color(44, 102, 230, 180);
    public Color pointColor = new Color(100, 100, 100, 180);
	
    public LineData(List<Double> scores) {
		this.scores = scores;
	}
    
    public LineData(List<Double> scores, Color lineColor, Color pointColor) {
		this.scores = scores;
		this.lineColor = lineColor;
		this.pointColor = pointColor;
	}
    
    public List<Point> getPoints(int width, int height, double maxScore, double minScore) {
    	double xScale = ((double) width - (2 * GraphPanel2.padding) - GraphPanel2.labelPadding) / (scores.size() - 1);
        double yScale = ((double) height - 2 * GraphPanel2.padding - GraphPanel2.labelPadding) / (maxScore - minScore);
        
    	List<Point> graphPoints = new ArrayList<>();
        for (int index = 0; index < scores.size(); index++) {
            int x1 = (int) (index * xScale + GraphPanel2.padding + GraphPanel2.labelPadding);
            int y1 = (int) ((maxScore - scores.get(index)) * yScale + GraphPanel2.padding);
            graphPoints.add(new Point(x1, y1));
        }
        return graphPoints;
    }
    
    public void draw(Graphics2D g2, int width, int height, double maxScore, double minScore) {
    	List<Point> graphPoints = getPoints(width, height, maxScore, minScore);
    	
    	Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int index = 0; index < graphPoints.size() - 1; index++) {
            int x1 = graphPoints.get(index).x;
            int y1 = graphPoints.get(index).y;
            int x2 = graphPoints.get(index + 1).x;
            int y2 = graphPoints.get(index + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
    	
        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int index = 0; index < graphPoints.size(); index++) {
            int x = graphPoints.get(index).x - pointWidth / 2;
            int y = graphPoints.get(index).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }
}
