package com.kannect.achieve.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionDTO {
	    private Long id;

	    private Long giverId;

	    private Long receiverId;

	    private String message;

	    private LocalDateTime dateGiven;

	    private Long badgeId;
	    
	    private Boolean approved;
}
