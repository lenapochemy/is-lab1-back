package org.example.labbb1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDTO {

    private Long id;
    private Integer x;
    private Float y;
    private UserDTO userDTO;
}
