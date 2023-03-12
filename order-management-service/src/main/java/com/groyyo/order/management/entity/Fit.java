package com.groyyo.order.management.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "fit")
@Table(name = "fit", uniqueConstraints = {@UniqueConstraint(name = "UK_fit_name", columnNames = {"name"})})
public class Fit extends AbstractJpaEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
    private String name;

    @Column(name = "master_id", columnDefinition = "varchar(40)", nullable = true)
	private String masterId;
    @Column(columnDefinition = "varchar(40)", nullable = false)
    private String factoryId;
}
