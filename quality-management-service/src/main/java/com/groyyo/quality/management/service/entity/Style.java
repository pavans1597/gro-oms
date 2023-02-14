package com.groyyo.quality.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="style")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder

public class Style extends AbstractJpaEntity {


    @Column(name="name", columnDefinition = "varchar(100)",nullable = false)
    private String name;
    @Column(name = "style_number", columnDefinition = "varchar(100)",nullable = false)
    private String styleNumber;

}
