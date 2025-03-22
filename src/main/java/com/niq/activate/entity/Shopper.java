package com.niq.activate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;


@Entity
@Table(name = "shoppers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shopper {

    @Id
    @NotBlank
    @Column(name = "shopper_id")
    private String shopperId;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdOn;
}