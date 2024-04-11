package com.example.iTIME.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="tbl_permission_type")
public class PermissionTypeEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    @OneToMany(mappedBy = "permissionTypeId")
    List<PermissionTransactionEntity> permissionTransactionEntityList;
}
