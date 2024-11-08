package org.example.labbb1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {

    private Long id;
    private String name;
    private String parentLegion;
    private UserDTO userDTO;
}
