/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import static Map.ApiKey.ApiKey;
import static carmeter.CarMeter.flag_position;
import static carmeter.CarMeter.latitude;
import static carmeter.CarMeter.longitude;

/*Import Jar files for Google Map*/
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEvent;

/**
 *
 * @author Abdullah Hanfy
 */
public class GMap implements MapComponentInitializedListener {
    public static Thread t;

    GoogleMapView mapView;
    GoogleMap map;

    MarkerOptions markerOptions;
    Marker marker;
    MapOptions mapOptions;

    public GMap() {
        mapView = new GoogleMapView("en", ApiKey);
        mapView.setKey(ApiKey);

        mapView.addMapInitializedListener(this);
        mapView.setDisableDoubleClick(true);
        mapView.getWebview().getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent event) {
            }
        });

    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        mapOptions = new MapOptions();
        mapOptions.center(new LatLong(30.033333, 31.233334))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(13);

        map = mapView.createMap(mapOptions);

        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLong(30.033333, 30.033333))
                .visible(Boolean.TRUE)
                .title("My Marker");

        marker = new Marker(markerOptions);
//        if (flag_position == 1) {
//            map.addMarker(marker);
//        } else {
//            map.removeMarker(marker);
//        }

         t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                    Platform.runLater(() -> {
                        ////////////////////////    TODO pelase handle the showing of marker at start of applicaiton\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                        if (flag_position == 1 && latitude > 10.0) {
                            map.removeMarker(marker);
                            markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLong(latitude, longitude))
                                    .visible(Boolean.TRUE)
                                    .title("My Marker");
                            marker = new Marker(markerOptions);
                            map.addMarker(marker);
                            map.setCenter(new LatLong(latitude, longitude));
                        }
                        else
                        {
                             map.removeMarker(marker);
                             mapOptions.center(new LatLong(30.033333, 31.233334));
                        }

                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();

    }

    public void createUI(Pane parent) {
        BorderPane pane = new BorderPane();

        pane.setCenter(mapView);

        parent.getChildren().add(pane);
    }
    public void removeUI(Pane parent) {
        

        parent.getChildren().remove(mapView);
    }
}
