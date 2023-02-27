package com.groyyo.order.management.service.entity;

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
@Entity(name = "style")
@Table(name = "style", uniqueConstraints = {@UniqueConstraint(name = "UK_size_group_name", columnNames = {"name"})})
public class Style extends AbstractJpaEntity {
    @Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
    private String name;
    @Column(name = "style_number", columnDefinition = "varchar(100)", nullable = false)
    private String styleNumber;

}
