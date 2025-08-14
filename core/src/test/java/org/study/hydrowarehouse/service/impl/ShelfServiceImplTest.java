package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.ShelfDao;
import org.study.hydrowarehouse.entity.Dto.ShelfDto;
import org.study.hydrowarehouse.entity.Dto.StorageRackDto;
import org.study.hydrowarehouse.entity.Shelf;
import org.study.hydrowarehouse.entity.StorageRack;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.mapping.EntityResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelfServiceImplTest {

    @Mock
    private ShelfDao shelfDao;

    @Mock
    private DtoResolver dtoResolver;

    @Mock
    private EntityResolver entityResolver;

    @InjectMocks
    private ShelfServiceImpl shelfService;

    private Shelf shelf = new Shelf(1, "shelf");
    private ShelfDto shelfDto = new ShelfDto();
    private List<Shelf> shelfList = new ArrayList<>();
    private int id = 1;
    private String name = "name";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        shelfList.add(shelf);

        shelfDto.setShelfDtoId(1);
        shelfDto.setName("name");
    }

    @Test
    void create() {
        ShelfDto shelfDto = new ShelfDto();
        shelfDto.setName("Test Shelf");
        shelfDto.setStorageRackDto(new StorageRackDto());

        StorageRack mockedRack = new StorageRack();
        when(entityResolver.resolveStorageRack(any())).thenReturn(mockedRack);
        when(shelfDao.create(any(Shelf.class))).thenReturn(1); // Важно — возвращаем > 0

        boolean result = shelfService.create(shelfDto);

        assertTrue(result);
    }

    @Test
    void testUpdate_successful() throws CoreException {
        ShelfDto dto = new ShelfDto();
        dto.setShelfDtoId(1);
        dto.setName("Updated Shelf");

        Shelf existing = new Shelf();
        existing.setShelfId(1);
        existing.setName("Old Shelf");

        when(shelfDao.findById(1)).thenReturn(Optional.of(existing));
        when(shelfDao.update(existing)).thenReturn(true);

        boolean result = shelfService.update(dto);

        assertTrue(result);
        assertEquals("Updated Shelf", existing.getName());
        verify(shelfDao).update(existing);
    }

    @Test
    void testUpdate_shelfNotFound_throwsException() {
        ShelfDto dto = new ShelfDto();
        dto.setShelfDtoId(404);
        dto.setName("Shelf Name");

        when(shelfDao.findById(404)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> shelfService.update(dto));

        assertTrue(ex.getMessage().contains("Shelf not found"));
    }

    @Test
    void testUpdate_blankName_shouldKeepOriginal() throws CoreException {
        ShelfDto dto = new ShelfDto();
        dto.setShelfDtoId(1);
        dto.setName(" ");

        Shelf existing = new Shelf();
        existing.setShelfId(1);
        existing.setName("Existing Shelf");

        when(shelfDao.findById(1)).thenReturn(Optional.of(existing));
        when(shelfDao.update(existing)).thenReturn(true);

        boolean result = shelfService.update(dto);

        assertTrue(result);
        assertEquals("Existing Shelf", existing.getName());
        verify(shelfDao).update(existing);
    }

    @Test
    void findAll() {
        when(shelfDao.getAllShelf()).thenReturn(shelfList);
        when(dtoResolver.resolveShelfDto(shelf)).thenReturn(shelfDto);

        List<ShelfDto> shelfDtoList = shelfService.findAll();

        assertTrue(shelfDtoList.size() > 0);
        verify(shelfDao, times(1)).getAllShelf();
    }

    @Test
    void findById() {
        when(shelfDao.findById(id)).thenReturn(Optional.of(shelf));
        when(dtoResolver.resolveShelfDto(shelf)).thenReturn(shelfDto);
        Optional<ShelfDto> shelfDtoOptional = shelfService.findById(id);

        assertTrue(shelfDtoOptional.isPresent());
        verify(shelfDao, times(1)).findById(id);
    }

    @Test
    void findByName() {
        when(shelfDao.findByName(name)).thenReturn(Optional.of(shelf));
        when(dtoResolver.resolveShelfDto(shelf)).thenReturn(shelfDto);
        Optional<ShelfDto> shelfDtoOptional = shelfService.findByName(name);

        assertTrue(shelfDtoOptional.isPresent());
        verify(shelfDao, times(1)).findByName(name);
    }

    @Test
    void remove() {
        when(shelfDao.findById(id)).thenReturn(Optional.of(shelf));
        when(shelfDao.remove(id)).thenReturn(true);

        boolean condition = shelfService.remove(id);

        assertTrue(condition);
        verify(shelfDao, times(1)).remove(id);
    }

    @Test
    void findShelfByName() {
        when(shelfDao.findByName(name)).thenReturn(Optional.of(shelf));
        when(dtoResolver.resolveShelfDto(shelf)).thenReturn(shelfDto);
        Optional<Shelf> shelfOptional = shelfService.findShelfByName(name);

        assertTrue(shelfOptional.isPresent());
        verify(shelfDao, times(1)).findByName(name);
    }
}