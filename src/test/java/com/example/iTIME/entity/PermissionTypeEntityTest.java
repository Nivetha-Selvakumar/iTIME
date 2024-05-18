package com.example.iTIME.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PermissionTypeEntityTest {

    @InjectMocks
    PermissionTypeEntity permissionTypeEntity;

    @Test
    void permissionTest(){
        permissionTypeEntity.setId(1);
        permissionTypeEntity.setPermissionTransactionEntityList(new ArrayList<>());
        permissionTypeEntity.setPermissionName("ABC");

        int id = permissionTypeEntity.getId();
        List<PermissionTransactionEntity> permissionTransactionEntityList = permissionTypeEntity.getPermissionTransactionEntityList();
        String name= permissionTypeEntity.getPermissionName();

        Assertions.assertEquals(Optional.of(id), Optional.of(permissionTypeEntity.getId()));
        Assertions.assertEquals(permissionTransactionEntityList, permissionTypeEntity.getPermissionTransactionEntityList());
        Assertions.assertEquals(name, permissionTypeEntity.getPermissionName());

        PermissionTypeEntity permissionTypeEntity1 = new PermissionTypeEntity();

        assertNotNull(permissionTypeEntity1);
    }

}
