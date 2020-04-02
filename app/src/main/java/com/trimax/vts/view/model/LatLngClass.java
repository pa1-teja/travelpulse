package com.trimax.vts.view.model;

public class LatLngClass {
        public double lat;
        public double lng;

        public LatLngClass(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

    @Override
    public String toString() {
        return "LatLngClass{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}




