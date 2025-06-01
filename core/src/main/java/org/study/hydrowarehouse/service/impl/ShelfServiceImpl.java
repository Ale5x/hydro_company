package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ShelfDao;
import org.study.hydrowarehouse.entity.Dto.ShelfDto;
import org.study.hydrowarehouse.entity.Shelf;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ShelfService;
import org.study.hydrowarehouse.utill.filestorage.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShelfServiceImpl extends EntityMapper<ShelfDto, Shelf> implements ShelfService {

    private final static String SHELF_BY_ID_NOT_FOUND_MESSAGE = "Shelf not found. [id = %s, name = %s]";

    private final ShelfDao shelfDao;

    @Autowired
    public ShelfServiceImpl(ShelfDao shelfDao) {
        this.shelfDao = shelfDao;
    }

    @Override
    public boolean create(ShelfDto shelfDto) throws CoreException {
        Shelf shelf = new Shelf(shelfDto.getName());
        return shelfDao.create(shelf) > 0;
    }

    @Override
    public boolean update(ShelfDto shelfDto) throws CoreException {
        Shelf existingShelf = shelfDao.findById(shelfDto.getShelfDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(SHELF_BY_ID_NOT_FOUND_MESSAGE,
                            shelfDto.getShelfDtoId(), shelfDto.getName()));
                });
        existingShelf.setName(StringUtils.isBlankOrNullText(shelfDto.getName())
                ? existingShelf.getName() : shelfDto.getName());
        return shelfDao.update(existingShelf);
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
            throw new CoreException(SHELF_BY_ID_NOT_FOUND_MESSAGE);
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
}
