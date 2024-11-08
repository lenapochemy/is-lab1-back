package org.example.labbb1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.labbb1.model.EditType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCoordinatesDTO {
    private Long id;
    private CoordinatesDTO coordinatesDTO;
    private UserDTO userDTO;
    private EditType editType;
    private LocalDateTime date;
}
