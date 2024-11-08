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
public class EditChapterDTO {
    private Long id;
    private ChapterDTO chapterDTO;
    private UserDTO userDTO;
    private EditType type;
    private LocalDateTime date;
}
