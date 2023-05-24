package org.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Hit {

    @Id
    @Column(name = "hit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    @NotBlank
    String app;

    @Column
    @NotBlank
    String uri;

    @Column
    @NotBlank
    String ip;

    @Column
    @NotBlank
    LocalDateTime timestamp;
}
