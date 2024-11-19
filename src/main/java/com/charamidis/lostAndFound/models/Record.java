package com.charamidis.lostAndFound.models;

import java.io.Serializable;


public class Record implements Serializable {
   private Integer id ;
   private String record_date;
   private Integer officer_id;
   private String founder_last_name;
   private String founder_first_name;
   private String founder_id_number;
   private String founder_telephone;
   private String founder_street_address;
   private String founder_street_number;
   private String founder_father_name;
   private String founder_area_inhabitant;
   private String found_date;
   private String found_location;
   private String item_description;
   private String found_time;
   private String record_time;
   private String uuid;

    public Record(){

    }

    public Record(String uuid,Integer id, String record_date,String record_time, Integer officer_id, String founder_last_name, String founder_first_name, String founder_id_number, String founder_telephone, String founder_street_address, String founder_street_number, String founder_father_name, String founder_area_inhabitant, String found_date,String found_time, String found_location, String item_description) {
        this.uuid = uuid;
        this.id = id;
        this.record_date=record_date;
        this.record_time=record_time;
        this.officer_id = officer_id;
        this.founder_last_name = founder_last_name;
        this.founder_first_name = founder_first_name;
        this.founder_id_number = founder_id_number;
        this.founder_telephone = founder_telephone;
        this.founder_street_address = founder_street_address;
        this.founder_street_number = founder_street_number;
        this.founder_father_name = founder_father_name;
        this.founder_area_inhabitant = founder_area_inhabitant;
        this.found_date = found_date;
        this.found_time = found_time;
        this.found_location = found_location;
        this.item_description = item_description;
    }

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public Integer getOfficer_id() {
        return officer_id;
    }

    public void setOfficer_id(Integer officer_id) {
        this.officer_id = officer_id;
    }

    public String getFounder_last_name() {
        return founder_last_name;
    }

    public void setFounder_last_name(String founder_last_name) {
        this.founder_last_name = founder_last_name;
    }

    public String getFounder_first_name() {
        return founder_first_name;
    }

    public void setFounder_first_name(String founder_first_name) {
        this.founder_first_name = founder_first_name;
    }

    public String getFounder_id_number() {
        return founder_id_number;
    }

    public void setFounder_id_number(String founder_id_number) {
        this.founder_id_number = founder_id_number;
    }

    public String getFounder_telephone() {
        return founder_telephone;
    }

    public void setFounder_telephone(String founder_telephone) {
        this.founder_telephone = founder_telephone;
    }

    public String getFounder_street_address() {
        return founder_street_address;
    }

    public void setFounder_street_address(String founder_street_address) {
        this.founder_street_address = founder_street_address;
    }

    public String getFounder_street_number() {
        return founder_street_number;
    }

    public void setFounder_street_number(String founder_street_number) {
        this.founder_street_number = founder_street_number;
    }

    public String getFounder_father_name() {
        return founder_father_name;
    }

    public void setFounder_father_name(String founder_father_name) {
        this.founder_father_name = founder_father_name;
    }

    public String getFounder_area_inhabitant() {
        return founder_area_inhabitant;
    }

    public void setFounder_area_inhabitant(String founder_area_inhabitant) {
        this.founder_area_inhabitant = founder_area_inhabitant;
    }

    public String getFound_date() {
        return found_date;
    }

    public void setFound_date(String found_date) {
        this.found_date = found_date;
    }

    public String getFound_location() {
        return found_location;
    }

    public void setFound_location(String found_location) {
        this.found_location = found_location;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getFound_time() {
        return found_time;
    }

    public void setFound_time(String found_time) {
        this.found_time = found_time;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(String record_time) {
        this.record_time = record_time;
    }

    @Override
    public String toString() {
        return item_description;
    }
}
