package com.example.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Serdeable
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age")
    @EqualsAndHashCode.Exclude
    private Integer age;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}
