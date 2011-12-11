package edu.ict.rgeothes.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("rectangle")
public class Rectangle extends Location {

	private Point point1;
	private Point point2;
	
	
	public Rectangle() {
	}


	public Point getPoint1() {
		return point1;
	}


	public void setPoint1(Point point1) {
		this.point1 = point1;
	}


	public Point getPoint2() {
		return point2;
	}


	public void setPoint2(Point point2) {
		this.point2 = point2;
	}
	
		
	
}
