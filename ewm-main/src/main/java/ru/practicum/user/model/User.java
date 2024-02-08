package ru.practicum.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.rating.model.Rating;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

/**
 * Class with user's components
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    @Email
    private String email;
    private String name;
    private Double rating;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "user_ratings",
            joinColumns = {@JoinColumn(name = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "id_rating")})
    private List<Rating> userRatings;
}
