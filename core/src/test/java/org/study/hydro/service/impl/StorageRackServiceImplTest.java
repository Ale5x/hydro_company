package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.hydro.dao.StorageRackDao;
import org.study.hydro.entity.Dto.ShelfDto;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.entity.Shelf;
import org.study.hydro.entity.StorageRack;
import org.study.hydro.service.ShelfService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StorageRackServiceImplTest {

    @Mock
    private ShelfService shelfService;
    @Mock
    private StorageRackDao storageRackDao;

    @InjectMocks
    private StorageRackServiceImpl storageRackService;

    private ShelfDto shelfDto = new ShelfDto();
    private Shelf shelf = new Shelf(1, "name");
    private StorageRack storageRack = new StorageRack("name");
    private StorageRackDto storageRackDto = new StorageRackDto();
    private List<StorageRack> storageList = new ArrayList<>();
    private int id = 1;

    private int limit = 10;
    private int offset = 0;
    private String name = "Name";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        storageRack.setShelf(shelf);

        storageList.add(storageRack);
        shelfDto.setName("name");
        shelfDto.setShelfDtoId(1);

//        storageRackDto.setStorageRackDtoId(1);
        storageRackDto.setName(storageRack.getName());
        storageRackDto.setShelfName(shelf.getName());
    }

    @Test
    void create() {
        when(shelfService.findShelfByName(any(String.class))).thenReturn(Optional.of(shelf));
        when(storageRackDao.create(any(StorageRack.class))).thenReturn(true);

//        storageRackDto.setStorageRackDtoId(0);

        boolean condition = storageRackService.create(storageRackDto);

        System.out.println("Storage rack dao" + storageRackDto);
        assertTrue(condition);
        verify(shelfService, times(1)).findShelfByName(shelf.getName());
        verify(storageRackDao, times(1)).create(any(StorageRack.class));
    }

    @Test
    void update() {
        when(shelfService.findShelfByName(any(String.class))).thenReturn(Optional.of(shelf));
        when(storageRackDao.update(any(StorageRack.class))).thenReturn(true);

        boolean condition = storageRackService.update(storageRackDto);

        assertTrue(condition);
        verify(shelfService, times(1)).findShelfByName(shelf.getName());
        verify(storageRackDao, times(1)).update(any(StorageRack.class));
    }

    @Test
    void findById() {
        when(storageRackDao.getStorageRackById(id)).thenReturn(Optional.of(storageRack));

        Optional<StorageRackDto> storageRackDtoOptional = storageRackService.findById(id);

        assertTrue(storageRackDtoOptional.isPresent());
        verify(storageRackDao, times(1)).getStorageRackById(id);
    }

    @Test
    void storageRackList() {
        when(storageRackDao.getStorageRacksList(limit, offset)).thenReturn(storageList);

        List<StorageRackDto> list = storageRackService.storageRackList(limit, offset);

        assertNotNull(list);
        verify(storageRackDao, times(1)).getStorageRacksList(limit, offset);
    }
}