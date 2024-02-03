package ru.practicum.model;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class with statistics components
 */
@Data
@Builder
@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "service")
    private String app;
    private String uri;
    private String ip;
    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;
}
