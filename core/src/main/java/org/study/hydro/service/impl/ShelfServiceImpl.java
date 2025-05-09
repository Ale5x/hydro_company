package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.ShelfDao;
import org.study.hydro.entity.Dto.ShelfDto;
import org.study.hydro.entity.Shelf;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.ShelfService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShelfServiceImpl extends EntityMapper<ShelfDto, Shelf> implements ShelfService {

    private final ShelfDao shelfDao;

    private final static String SHELF_BY_ID_NOT_FOUND_ERROR = "Shelf by id not found.";
    private final static String SHELF_BY_NAME_NOT_FOUND_ERROR = "Shelf by name not found.";

    @Autowired
    public ShelfServiceImpl(ShelfDao shelfDao) {
        this.shelfDao = shelfDao;
    }

    @Override
    public boolean update(ShelfDto shelfDto) throws CoreException {
        return shelfDao.update(mapToEntityFromDto(shelfDto, true));
    }

    @Override
    public boolean create(ShelfDto shelfDto) throws CoreException {
        return shelfDao.create(mapToEntityFromDto(shelfDto, false)) > 0;
    }

    @Override
    public Optional<ShelfDto> findById(int id) throws CoreException {
        Optional<Shelf> shelf = shelfDao.findById(id);
        if (shelf.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return shelf.map(this::mapToObjectDto);
    }

    @Override
    public Optional<ShelfDto> findByName(String name) throws CoreException {
        Optional<Shelf> shelf = shelfDao.findByName(name);
        if (shelf.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return shelf.map(this::mapToObjectDto);
    }

    @Override
    public List<ShelfDto> findAll() throws CoreException {
        return mapToListObjectsDto(shelfDao.getAllShelf());
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Optional<Shelf> shelf = shelfDao.findById(id);
        if (shelf.isPresent()) {
            return shelfDao.remove(id);
        } else {
            throw new CoreException(SHELF_BY_ID_NOT_FOUND_ERROR);
        }
    }

    @Override
    public Optional<Shelf> findShelfByName(String name) throws CoreException {
        return shelfDao.findByName(name);
    }

    @Override
    public List<ShelfDto> mapToListObjectsDto(List<Shelf> objectsList) {
        List<ShelfDto> shelfDtoList = new ArrayList<>();
        for (Shelf shelf : objectsList) {
            shelfDtoList.add(mapToObjectDto(shelf));
        }
        return shelfDtoList;
    }

    @Override
    public ShelfDto mapToObjectDto(Shelf object) {
        ShelfDto shelfDto = new ShelfDto();

        shelfDto.setShelfDtoId(object.getShelfId());
        shelfDto.setName(object.getName());
        return shelfDto;
    }

    @Override
    public Shelf mapToEntityFromDto(ShelfDto objectDto, boolean isUpdate) {
        Shelf shelf = new Shelf();

        if (isUpdate) {
            shelf.setShelfId(objectDto.getShelfDtoId());
        }

        shelf.setName(objectDto.getName());
        return shelf;
    }
}
