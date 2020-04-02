package com.trimax.vts.view.model.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteResponse {
    @SerializedName("RouteLinks")
    @Expose
    private List<RouteLink> routeLinks = null;
    @SerializedName("TracePoints")
    @Expose
    private List<TracePoint> tracePoints = null;
    @SerializedName("Warnings")
    @Expose
    private List<Warning> warnings = null;
    @SerializedName("MapVersion")
    @Expose
    private String mapVersion;

    public List<RouteLink> getRouteLinks() {
        return routeLinks;
    }

    public void setRouteLinks(List<RouteLink> routeLinks) {
        this.routeLinks = routeLinks;
    }

    public List<TracePoint> getTracePoints() {
        return tracePoints;
    }

    public void setTracePoints(List<TracePoint> tracePoints) {
        this.tracePoints = tracePoints;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }

    public String getMapVersion() {
        return mapVersion;
    }

    public void setMapVersion(String mapVersion) {
        this.mapVersion = mapVersion;
    }
}
