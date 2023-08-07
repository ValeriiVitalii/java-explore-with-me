package practicum.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import practicum.utility.EventState;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 2000, nullable = false)
    String annotation;

    @ManyToOne
    @CollectionTable(name = "events_category", joinColumns = @JoinColumn(name = "category_id", nullable = false))
    Category category;

    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;

    @Column(length = 7000, nullable = false)
    String description;

    @Column(name = "event_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    LocalDateTime eventDate;

    @ManyToOne
    @CollectionTable(name = "events_initiator", joinColumns = @JoinColumn(name = "initiator_id", nullable = false))
    User initiator;

    @Column(name = "location_lat", nullable = false)
    float lat;

    @Column(name = "location_lon", nullable = false)
    float lon;

    @Column(nullable = false)
    boolean paid;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "published_on")
     LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    EventState state;

    @Column(length = 120)
    String title;

}