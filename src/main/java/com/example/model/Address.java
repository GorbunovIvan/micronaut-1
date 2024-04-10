package com.example.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "addresses",
        uniqueConstraints = @UniqueConstraint(columnNames = { "country", "city", "street", "code" })
)
@Serdeable
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "code", nullable = false)
    private Integer code;
}
