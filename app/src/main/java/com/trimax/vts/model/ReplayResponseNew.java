package com.trimax.vts.model;

import com.trimax.vts.view.model.LatLngClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReplayResponseNew  implements Serializable {
    private String status;
    private List<LatLngClass> calculatedPoints;
    private ArrayList<ArrayList<LiveTrakingResponseNew>> actStructrdPnts;
    private List<LiveTrakingResponseNew> actualPoints;
    private Double total_distance;
}
