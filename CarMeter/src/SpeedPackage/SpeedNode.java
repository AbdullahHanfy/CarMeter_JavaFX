
package SpeedPackage;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Ahmed Sherif
 */
public class SpeedNode {

    Gauge gauge;

    public SpeedNode() {
        gauge = GaugeBuilder.create().minValue(0).maxValue(220)
                .skinType(Gauge.SkinType.DIGITAL)
                .foregroundBaseColor(Color.rgb(0, 222, 249))
                .barColor(Color.rgb(0, 222, 249))
                .title("Speed")
                .unit("Km / h")
                .animated(true)
                .build();
    }

  

    public void setSpeed(double speed) {
        gauge.setValue(speed);
    }
    
    public void createUI(Pane parent) {
        BorderPane pane = new BorderPane();

        pane.setCenter(gauge);

        parent.getChildren().add(pane);
    }
}
