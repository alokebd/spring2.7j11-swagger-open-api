package com.vision.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponseDto {
	private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private List<BookResponseDto> books;
}
