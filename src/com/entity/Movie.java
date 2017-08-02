package com.entity;

public class Movie {
	private int id;
	private String name;
	private int year;
	private String country;
	private String type;
	private double rent;
	private double rating;
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId(){
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public void setRent(Double rent) {
		this.rent = rent;
	}

	public Double getRent() {
		return rent;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Double getRating() {
		return rating;
	}

}
