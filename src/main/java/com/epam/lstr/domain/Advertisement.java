package com.epam.lstr.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "advertisement")
public class Advertisement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advert_seq")
    @SequenceGenerator(name = "advert_seq", sequenceName = "advert_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    @NotBlank(message = "Empty model is invalid")
    private String model;

    @NotBlank(message = "Empty title is invalid")
    private String title;

    @NotNull(message = "Empty year is invalid")
    @DecimalMin("1900")
    @DecimalMax("2021")
    private Integer year;

    @NotNull(message = "Empty mileage is invalid")
    @DecimalMin("0")
    private Integer mileage;

    private boolean active;

    @NotNull(message = "Empty price is invalid")
    @DecimalMin("0")
    private Integer price;

    @ManyToOne(optional=false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private BaseUser user;

    @OneToMany(mappedBy = "advertisement",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<AdResponse> adResponses = new HashSet<>();

    public void clearAdResponses() {
        adResponses.clear();
    }

    public void addAdResponse(AdResponse response) {
        adResponses.add(response);
        response.setAdvertisement(this);
    }

    public List<AdResponse> getAdResponse() {
        return new ArrayList<>(adResponses);
    }
}
