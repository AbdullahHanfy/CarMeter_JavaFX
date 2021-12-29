/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gaugepkg;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.scene.paint.Color;

/**
 *
 * @author WINDOWS
 */
public class GaugeClass {
    private Gauge gauge;
    
    public Gauge setGauge()
    {
        gauge = GaugeBuilder.create().minValue(0).maxValue(220)
                              .skinType(Gauge.SkinType.DIGITAL)
                              .foregroundBaseColor(Color.rgb(0, 222, 249))
                              .barColor(Color.rgb(0, 222, 249))
                              .title("Speed")
                              .unit("Km / h")
                              .animated(true)
                              .build();
        
        return gauge;
    }
    public void setSpeed(double speed)
    {
        gauge.setValue(speed);
    }
}
