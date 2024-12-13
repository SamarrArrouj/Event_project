package com.example.Event_Project_Spring.service;
import com.example.Event_Project_Spring.entities.Event;
import com.example.Event_Project_Spring.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;



}
