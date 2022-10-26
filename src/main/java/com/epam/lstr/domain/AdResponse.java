package com.epam.lstr.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "ad_response")
public class AdResponse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ad_response_seq")
    @SequenceGenerator(name = "ad_response_seq", sequenceName = "ad_response_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    @Column(name = "my_price")
    private Integer myPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private BaseUser user;

    @ManyToOne(optional=false)
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdResponse that = (AdResponse) o;
        return user.equals(that.user) && advertisement.equals(that.advertisement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, advertisement);
    }
}
