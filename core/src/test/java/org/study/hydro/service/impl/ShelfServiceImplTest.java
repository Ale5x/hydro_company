package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.hydro.dao.ShelfDao;
import org.study.hydro.entity.Dto.ShelfDto;
import org.study.hydro.entity.Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void update() {
        when(shelfDao.update(any(Shelf.class))).thenReturn(true);
        boolean condition = shelfService.update(shelfDto);

        assertTrue(condition);
        verify(shelfDao, times(1)).update(shelf);
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