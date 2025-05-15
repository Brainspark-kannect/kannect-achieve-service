package com.kannect.achieve.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recognitions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recognition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_user_id", nullable = false)
    private Long giverId;

    @Column(name = "receiver_user_id", nullable = false)
    private Long receiverId;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime dateGiven;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badge badge;
    
    @Column(name = "approved", nullable = false)
    private Boolean approved;

}
