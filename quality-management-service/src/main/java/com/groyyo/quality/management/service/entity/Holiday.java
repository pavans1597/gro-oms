package com.groyyo.quality.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="holiday")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Holiday extends AbstractJpaEntity {

    @Column(name="name",nullable = false,columnDefinition = "char(40)")

    private String name;
    @Column(name="short_name",nullable = false,columnDefinition = "char(40)")
    private String shortName;
    @Column(name="date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;




}
