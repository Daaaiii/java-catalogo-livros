package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    @JsonAlias("name")
    private String name;
    private int birthYear;
    private Integer deathYear;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isAlive(int year) {
        if (deathYear == null) {
            return true;
        } else {
            return year >= birthYear && year <= deathYear;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
