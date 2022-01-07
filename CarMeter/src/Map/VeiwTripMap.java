/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import static Map.ApiKey.ApiKey;


/*Import Jar files for Google Map*/
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.*;
import javafx.application.Platform;

import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEvent;

/**
 *
 * @author Abdullah Hanfy
 */
public class VeiwTripMap implements MapComponentInitializedListener {

    GoogleMapView mapView;
    GoogleMap map;

    MapOptions mapOptions;
    MarkerOptions markerOptionsStart;
    MarkerOptions markerOptionsEnd;

    double lat_start;
    double lng_start;
    double lat_end;
    double lng_end;

    public VeiwTripMap(double lat_start,
            double lng_start,
            double lat_end,
            double lng_end) {
        this.lat_start = lat_start;
        this.lng_start = lng_start;

        this.lat_end = lat_end;
        this.lng_end = lng_end;

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
                .zoom(15);

        map = mapView.createMap(mapOptions);
        System.out.println(this.lat_end);

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                    Platform.runLater(() -> {
                        ////////////////////////    TODO pelase handle the showing of marker at start of applicaiton\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                        System.out.println("start Data od my location");
                        System.out.println(this.lat_start);
                        System.out.println(this.lng_start);
                        System.out.println(this.lat_end);
                        System.out.println(this.lng_end);
                        System.out.println("END Data od my location");
                        markerOptionsStart = new MarkerOptions();
                        markerOptionsStart.position(new LatLong(this.lat_start, this.lng_start)).visible(Boolean.TRUE).title("Start").label("S");
                        Marker markerStart = new Marker(markerOptionsStart);
                        map.addMarker(markerStart);

                        markerOptionsEnd = new MarkerOptions();
                        markerOptionsEnd.position(new LatLong(this.lat_end, this.lng_end)).visible(Boolean.TRUE).title("End").label("E");
                        Marker markerEnd = new Marker(markerOptionsEnd);
                        map.addMarker(markerEnd);
                    

                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();

//        MarkerOptions markerOptionsEnd = new MarkerOptions();
//        markerOptionsEnd.position(new LatLong(lat_end,lng_end)).visible(Boolean.TRUE).title("End").label("E");
//        Marker markerEnd = new Marker(markerOptionsEnd);
//        map.addMarker(markerEnd);
    }

    public void createUI(Pane parent) {
//        BorderPane pane = new BorderPane();
//
//        pane.setCenter(mapView);

        parent.getChildren().add(mapView);
    }

    public void start_Marker() {
        // Add markers to the map
        markerOptionsStart = new MarkerOptions();
        markerOptionsStart.position(new LatLong(this.lat_start, this.lng_start)).visible(Boolean.TRUE).title("Start").label("S");
        Marker markerStart = new Marker(markerOptionsStart);
        map.addMarker(markerStart);
    }

    public void end_Marker() {
        markerOptionsEnd = new MarkerOptions();
        markerOptionsEnd.position(new LatLong(lat_end, lng_end)).visible(Boolean.TRUE).title("End").label("E");
        Marker markerEnd = new Marker(markerOptionsEnd);
        map.addMarker(markerEnd);
    }

    public void removeUI(Pane parent) {

        parent.getChildren().remove(mapView);
    }
}
