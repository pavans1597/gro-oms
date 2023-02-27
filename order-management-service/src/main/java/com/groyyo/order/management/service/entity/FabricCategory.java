package com.groyyo.order.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "fabric_category")
@Table(name = "fabric_category", uniqueConstraints = {@UniqueConstraint(name = "UK_fabric_category_name", columnNames = {"name"})})
public class FabricCategory extends AbstractJpaEntity {
    @Column(name = "name", nullable = false, columnDefinition = "char(40)")

    private String name;
    @Column(name = "type", nullable = false, columnDefinition = "char(40)")

    private String type;

}
