package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydro.dao.ShelfDao;
import org.study.hydro.entity.Dto.ShelfDto;
import org.study.hydro.entity.Shelf;
import org.study.hydro.exception.CoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelfServiceImplTest {

    @Mock
    private ShelfDao shelfDao;

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
        Shelf sendShelf = new Shelf(0, "create");
        ShelfDto sendShelfDto = new ShelfDto();
        sendShelfDto.setName(sendShelf.getName());

        when(shelfDao.create(any(Shelf.class))).thenReturn(1);

        boolean condition = shelfService.create(sendShelfDto);

        assertTrue(condition);
        verify(shelfDao, times(1)).create(sendShelf);
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

        List<ShelfDto> shelfDtoList = shelfService.findAll();

        assertTrue(shelfDtoList.size() > 0);
        verify(shelfDao, times(1)).getAllShelf();
    }

    @Test
    void findById() {
        when(shelfDao.findById(id)).thenReturn(Optional.of(shelf));

        Optional<ShelfDto> shelfDtoOptional = shelfService.findById(id);

        assertTrue(shelfDtoOptional.isPresent());
        verify(shelfDao, times(1)).findById(id);
    }

    @Test
    void findByName() {
        when(shelfDao.findByName(name)).thenReturn(Optional.of(shelf));

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
        Optional<Shelf> shelfOptional = shelfService.findShelfByName(name);

        assertTrue(shelfOptional.isPresent());
        verify(shelfDao, times(1)).findByName(name);
    }
}