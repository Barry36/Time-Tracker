package com.cs446.group18.timetracker.entity;

/*
Study 1
Sleep 2
Chores 3
School 4
Gaming 5
Exercise 6
Selfcare 7
Groceries 8
Travel 9
Entertainement 10
Appointment 11
Shopping 12
Religion 13
Transit 14
Miscellaneous 20
*/

public class PlaceMapping {
    private Mapping[] mappings;

    public PlaceMapping() {
        mappings[1] = new Mapping(new Event(1, "study", ""), "library, school, secondary_school");
        mappings[3] = new Mapping(new Event(3, "chores", ""), "laundry");
        mappings[4] = new Mapping(new Event(4, "school", ""), "primary_school, school, secondary_school, university");
        mappings[6] = new Mapping(new Event(6, "exercise", ""), "gym, park, stadium");
        mappings[7] = new Mapping(new Event(7, "selfcare", ""), "beauty_salon, hair_care, spa");
        mappings[8] = new Mapping(new Event(8, "groceries", ""), "supermarket");
        mappings[9] = new Mapping(new Event(9, "travel", ""), "airport, embassy, travel_agency");
        mappings[10] = new Mapping(new Event(10, "entertainement", ""), "amusement_park, aquarium, art_gallery, bar, bowling_alley, campground, casino, liquor_store, movie_rental, movie_theater, museum, night_club, restaurant, spa, tourist_attraction, zoo");
        mappings[11] = new Mapping(new Event(11, "appointment", ""), "accounting, bank, car_dealer, car_rental, car_repair, car_wash, city_hall, courthouse, dentist, drugstore, electrician, fire_station, funeral_home, gas_station, hospital, insurance_agency, lawyer, pharmacy, physiotherapist, plumber, police, post_office, real_estate_agency, roofing_contractor, rv_park, veterinary_care");
        mappings[12] = new Mapping(new Event(12, "shopping", ""), "atm, book_store, bicycle_store, department_store, electronics_store, florist, hardware_store, home_goods_store, jewelry_store, shoe_store, shopping_mall");
        mappings[13] = new Mapping(new Event(13, "religion", ""), "church, hindu_temple, mosque, synagogue");
        mappings[14] = new Mapping(new Event(14, "transit", ""), "bus_station, light_rail_station, subway_station, transit_station, train_station");
    }

    public class Mapping {
        private Event event;
        private String place;

        public Mapping(Event e, String s) {
            event = e;
            place = s;
        }
    }
}
