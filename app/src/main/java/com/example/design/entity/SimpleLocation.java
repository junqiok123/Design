package com.example.design.entity;

public class SimpleLocation {
	private static String time;
	private static int error_code;
	private static double latitude;
	private static double longitude;
	private static float radius;
	private static float speed;
	private static int satellite;
	private static String addr;
	private static String city;
	private static String district ;

	public static String getTime() {
		return time;
	}

	public static void setTime(String time) {
		SimpleLocation.time = time;
	}

	public static int getError_code() {
		return error_code;
	}

	public static void setError_code(int error_code) {
		SimpleLocation.error_code = error_code;
	}

	public static double getLatitude() {
		return latitude;
	}

	public static void setLatitude(double latitude) {
		SimpleLocation.latitude = latitude;
	}

	public static double getLongitude() {
		return longitude;
	}

	public static void setLongitude(double longitude) {
		SimpleLocation.longitude = longitude;
	}

	public static float getRadius() {
		return radius;
	}

	public static void setRadius(float radius) {
		SimpleLocation.radius = radius;
	}

	public static String getAddr() {
		return addr;
	}

	public static void setAddr(String addr) {
		SimpleLocation.addr = addr;
	}

	public static String getCity() {
		return city;
	}

	public static void setCity(String city) {
		SimpleLocation.city = city;
	}

	public static String getDistrict() {
		return district;
	}

	public static void setDistrict(String district) {
		SimpleLocation.district = district;
	}

	public static float getSpeed() {
		return speed;
	}

	public static void setSpeed(float speed) {
		SimpleLocation.speed = speed;
	}

	public static int getSatellite() {
		return satellite;
	}

	public static void setSatellite(int satellite) {
		SimpleLocation.satellite = satellite;
	}

}
