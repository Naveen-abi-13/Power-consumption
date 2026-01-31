/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package power;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author admin
 */
public class Graph1
{
    Details dt=new Details();
    public void display1(double val1,double val2)
    {
        try
        {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.setValue(val1, "PSO" ,"Execution Time");
            dataset.setValue(val2, "ALO" ,"Execution Time");
            
                  
            
            
            JFreeChart chart = ChartFactory.createBarChart
  
            ("Execution Time","", "Time in ms", dataset, 
  
            PlotOrientation.VERTICAL, true,true, false);
            
            chart.getTitle().setPaint(Color.blue); 
  
            CategoryPlot p = chart.getCategoryPlot(); 
  
            p.setRangeGridlinePaint(Color.red); 
            System.out.println("Range : "+p.getRangeAxisCount() );
  
  
            CategoryItemRenderer renderer = p.getRenderer();

            renderer.setSeriesPaint(0, Color.red);
            renderer.setSeriesPaint(1, Color.green);
            
           // renderer.setSeriesPaint(3, Color.yellow);
            
  
  
            ChartFrame frame1=new ChartFrame("Execution Time",chart);
  
            frame1.setSize(400,400);
  
            frame1.setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void display2()
    {
        try
        {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.setValue(dt.ppow, "PSO" ,"Energy Consumption");
            dataset.setValue(dt.apow, "ALO" ,"Energy Consumption");
            
            
            
            
            
            JFreeChart chart = ChartFactory.createBarChart
  
            ("Energy Consumption","", "Value", dataset, 
  
            PlotOrientation.VERTICAL, true,true, false);
            
            chart.getTitle().setPaint(Color.blue); 
  
            CategoryPlot p = chart.getCategoryPlot(); 
  
            p.setRangeGridlinePaint(Color.red); 
            System.out.println("Range : "+p.getRangeAxisCount() );
  
  
            CategoryItemRenderer renderer = p.getRenderer();

            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesPaint(1, Color.pink);
            
           // renderer.setSeriesPaint(3, Color.yellow);
            
  
  
            ChartFrame frame1=new ChartFrame("Energy Consumption",chart);
  
            frame1.setSize(400,400);
  
            frame1.setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
