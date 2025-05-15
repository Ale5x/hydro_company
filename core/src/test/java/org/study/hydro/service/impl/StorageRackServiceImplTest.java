package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydro.dao.StorageRackDao;
import org.study.hydro.entity.Dto.ShelfDto;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.entity.Shelf;
import org.study.hydro.entity.StorageRack;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.ShelfService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

        storageRackDto.setName(storageRack.getName());
        storageRackDto.setShelfName(shelf.getName());
    }

    @Test
    void create() {
        when(shelfService.findShelfByName(any(String.class))).thenReturn(Optional.of(shelf));
        when(storageRackDao.create(any(StorageRack.class))).thenReturn(3);

        boolean condition = storageRackService.create(storageRackDto);

        assertTrue(condition);
        verify(shelfService, times(1)).findShelfByName(shelf.getName());
        verify(storageRackDao, times(1)).create(any(StorageRack.class));
    }

    @Test
    void testUpdate_successful() throws CoreException {
        StorageRackDto dto = new StorageRackDto();
        dto.setStorageRackDtoId(1);
        dto.setName("Updated Storage Rack");

        StorageRack existing = new StorageRack();
        existing.setStorageRackId(1);
        existing.setName("Old Storage Rack");

        when(storageRackDao.getStorageRackById(1)).thenReturn(Optional.of(existing));
        when(storageRackDao.update(existing)).thenReturn(true);

        boolean result = storageRackService.update(dto);

        assertTrue(result);
        assertEquals("Updated Storage Rack", existing.getName());
        verify(storageRackDao).update(existing);
    }

    @Test
    void testUpdate_storageRackNotFound_throwsException() {
        StorageRackDto dto = new StorageRackDto();
        dto.setStorageRackDtoId(404);
        dto.setName("Storage Rack");

        when(storageRackDao.getStorageRackById(404)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> storageRackService.update(dto));

        assertTrue(ex.getMessage().contains("Storage rack not found"));
    }

    @Test
    void testUpdate_blankName_shouldKeepOriginal() throws CoreException {
        StorageRackDto dto = new StorageRackDto();
        dto.setStorageRackDtoId(1);
        dto.setName(" ");

        StorageRack existing = new StorageRack();
        existing.setStorageRackId(1);
        existing.setName("Existing Storage Rack");

        when(storageRackDao.getStorageRackById(1)).thenReturn(Optional.of(existing));
        when(storageRackDao.update(existing)).thenReturn(true);

        boolean result = storageRackService.update(dto);

        assertTrue(result);
        assertEquals("Existing Storage Rack", existing.getName());
        verify(storageRackDao).update(existing);
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
        when(storageRackDao.getStorageRacksList(offset, limit)).thenReturn(storageList);

        List<StorageRackDto> list = storageRackService.storageRackList(offset, limit);

        assertNotNull(list);
        verify(storageRackDao, times(1)).getStorageRacksList(offset, limit);
    }
}