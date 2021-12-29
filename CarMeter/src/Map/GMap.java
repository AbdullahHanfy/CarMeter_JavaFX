/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;


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

    GoogleMapView mapView;
    GoogleMap map;

    MarkerOptions markerOptions;
    Marker marker;
    MapOptions mapOptions;
    
    public GMap() {
        mapView = new GoogleMapView("en", "AIzaSyA9_9-iMx5eq0xn1RKXl-6FSbGYZuIUPnk");
        mapView.setKey("AIzaSyA9_9-iMx5eq0xn1RKXl-6FSbGYZuIUPnk");
        
        mapView.addMapInitializedListener(this);
        mapView.setDisableDoubleClick(true);
        mapView.getWebview().getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent event) {}
        });

    }

    @Override
    public void mapInitialized() {
         //Set the initial properties of the map.
    mapOptions = new MapOptions();
    mapOptions.center(new LatLong(latitude, longitude))
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
    markerOptions.position( new LatLong(latitude, longitude) )
                .visible(Boolean.TRUE)
                .title("My Marker");

    marker = new Marker( markerOptions );
    map.addMarker(marker);
    map.removeMarker(marker);
                    
    Thread t = new Thread( () -> {
        while (true){
           try {
               Thread.sleep(2000);
               Platform.runLater(() -> {
                   
                       
                        map.removeMarker(marker);
                       markerOptions = new MarkerOptions();
                       markerOptions.position( new LatLong(latitude,longitude) )
                        .visible(Boolean.TRUE)
                        .title("My Marker");
                         marker = new Marker( markerOptions );
                        map.addMarker(marker);
                        map.setCenter(new LatLong(latitude, longitude));
                        
                   
                 
                       });
           } catch( Exception ex ) {
               ex.printStackTrace();
           }
    }});
        t.start();

        
        
       
    }
    
    
    public void createUI(Pane parent) {
        BorderPane pane = new BorderPane();
        
        pane.setCenter(mapView);

        parent.getChildren().add(pane);
    }
}
