package com.cs446.group18.timetracker.mapping;

import java.util.HashMap;
import java.util.Map;

public class PlaceMapping {
    private Map<String, String> mapping = new HashMap<>();

    public PlaceMapping() {
        mapping.put("Study", "library, school, secondary_school");
        mapping.put("Meal", "restaurant, cafe");
        mapping.put("Chores", "laundry");
        mapping.put("School",  "primary_school, school, secondary_school, university");
        mapping.put("Exercise",  "gym, park, stadium");
        mapping.put("Selfcare",  "beauty_salon, hair_care, spa, dentist");
        mapping.put("Groceries",  "supermarket");
        mapping.put("Travel",  "airport, embassy, travel_agency");
        mapping.put("Entertainment",  "amusement_park, aquarium, art_gallery, bar, bowling_alley, campground, casino, liquor_store, movie_rental, movie_theater, museum, night_club, spa, tourist_attraction, zoo");
        mapping.put("Appointment",  "accounting, bank, car_dealer, car_rental, car_repair, car_wash, city_hall, courthouse, dentist, drugstore, electrician, fire_station, funeral_home, gas_station, hospital, insurance_agency, lawyer, pharmacy, physiotherapist, plumber, police, post_office, real_estate_agency, roofing_contractor, rv_park, veterinary_care");
        mapping.put("Shopping",  "atm, book_store, bicycle_store, department_store, electronics_store, florist, hardware_store, home_goods_store, jewelry_store, shoe_store, shopping_mall, clothing_store");
        mapping.put("Religion",  "church, hindu_temple, mosque, synagogue");
        mapping.put("Transit",  "bus_station, light_rail_station, subway_station, transit_station, train_station");
    }

    public Map<String, String> getMapping() {
        return mapping;
    }
}

