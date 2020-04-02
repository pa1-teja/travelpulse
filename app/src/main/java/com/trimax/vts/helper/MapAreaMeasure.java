package com.trimax.vts.helper;

/**
 * MapAreaMeasure
 * 
 * @author ivanschuetz 
 */
public class MapAreaMeasure {
    
	public enum Unit {pixels, meters}
	
	public double value;
	public Unit unit;
	
	public MapAreaMeasure(double value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}
}