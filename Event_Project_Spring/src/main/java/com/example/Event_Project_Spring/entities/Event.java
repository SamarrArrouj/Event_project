package com.example.Event_Project_Spring.entities;
import com.example.Event_Project_Spring.ENUM.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String description;
    private String location;
    private LocalDate date;
    private int availablePlaces;
    private double price;
    private String image;
}
