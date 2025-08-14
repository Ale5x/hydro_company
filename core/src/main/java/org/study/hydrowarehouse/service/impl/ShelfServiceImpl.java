package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ShelfDao;
import org.study.hydrowarehouse.entity.Dto.ShelfDto;
import org.study.hydrowarehouse.entity.Dto.StorageRackDto;
import org.study.hydrowarehouse.entity.Shelf;
import org.study.hydrowarehouse.entity.StorageRack;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.mapping.EntityMapper;
import org.study.hydrowarehouse.mapping.EntityResolver;
import org.study.hydrowarehouse.service.ShelfService;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link Shelf} entities.
 * <p>
 * Provides business logic and transactional operations for creating, updating, retrieving, and deleting shelves within
 * storage racks. Extends {@link EntityMapper} to handle mapping between {@link ShelfDto} and {@link Shelf} entities.
 * </p>
 *
 * @see EntityMapper
 * @see ShelfService
 * @see Shelf
 * @see ShelfDto
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class ShelfServiceImpl extends EntityMapper<ShelfDto, Shelf> implements ShelfService {

    private final ShelfDao shelfDao;

    private final DtoResolver dtoResolver;
    private final EntityResolver entityResolver;

    @Autowired
    public ShelfServiceImpl(ShelfDao shelfDao, DtoResolver dtoResolver, EntityResolver entityResolver) {
        this.shelfDao = shelfDao;
        this.dtoResolver = dtoResolver;
        this.entityResolver = entityResolver;
    }

    @Override
    public boolean create(ShelfDto shelfDto) throws CoreException {
        Shelf shelf = new Shelf(shelfDto.getName());
        shelf.setStorageRack(entityResolver.resolveStorageRack(shelfDto.getStorageRackDto()));
        return shelfDao.create(shelf) > 0;
    }

    @Override
    public boolean update(ShelfDto shelfDto) throws CoreException {
        Shelf existingShelf = shelfDao.findById(shelfDto.getShelfDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                                                ExceptionMessages.SHELF_BY_ID_NOT_FOUND_MESSAGE,
                                                shelfDto.getShelfDtoId(),
                                                shelfDto.getName()));
                });
        existingShelf.setStorageRack(entityResolver.resolveStorageRack(shelfDto.getStorageRackDto()));
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
            //logger
            throw new CoreException(String.format(
                                ExceptionMessages.SHELF_BY_ID_NOT_FOUND_MESSAGE,
                                id,
                                ""));
        }
    }

    @Override
    public Optional<Shelf> findShelfByName(String name) throws CoreException {
        return shelfDao.findByName(name);
    }

    @Override
    public List<ShelfDto> mapToListObjectsDto(List<Shelf> objectsList)  throws CoreException {
        if (objectsList == null) return null;
        List<ShelfDto> shelfDtoList = new ArrayList<>();
        for (Shelf shelf : objectsList) {
            shelfDtoList.add(mapToObjectDto(shelf));
        }
        return shelfDtoList;
    }

    @Override
    public ShelfDto mapToObjectDto(Shelf object)  throws CoreException {
        if (object == null) return null;
        ShelfDto shelfDto = new ShelfDto();

        shelfDto.setShelfDtoId(object.getShelfId());
        shelfDto.setName(object.getName());

        shelfDto.setStorageRackDto(dtoResolver.resolveStorageRackDto(object.getStorageRack()));
        return shelfDto;
    }
}
