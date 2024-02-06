package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.List;

/**
 * Class with compilation's components
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private int id;
    @Column(name = "is_pinned")
    private boolean pinned;
    private String title;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "event_compilations",
            joinColumns = {@JoinColumn(name = "id_compilation")},
            inverseJoinColumns = {@JoinColumn(name = "id_event")})
    private List<Event> events;
}
