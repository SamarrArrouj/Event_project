package com.example.Event_Project_Spring.Dto;

import com.example.Event_Project_Spring.ENUM.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class EventDto {
    @NotEmpty(message = "The title is required")
    private String title;

    private Category category;

    @Size(min = 10, max = 2000, message = "The description should be between 10 and 2000 characters")
    private String description;

    @NotEmpty(message = "The location is required")
    private String location;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Min(value = 1, message = "Available places must be at least 1")
    private int availablePlaces;

    @Min(value = 0, message = "The price must be a positive value")
    private double price;

    private MultipartFile image;
}
