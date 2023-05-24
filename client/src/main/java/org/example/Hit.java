package org.example;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Hit {

    Integer id;

    String app;

    String uri;

    String ip;

    LocalDateTime timestamp;
}
