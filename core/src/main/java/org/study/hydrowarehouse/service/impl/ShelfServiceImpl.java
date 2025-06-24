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
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ShelfService;
import org.study.hydrowarehouse.utill.StringUtils;

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
        shelf.setStorageRack(mapToStorageRack(shelfDto.getStorageRackDto()));
        return shelfDao.create(shelf) > 0;
    }

    /**
     * Converts a {@link StorageRackDto} object into a {@link StorageRack} entity.
     *
     * <p>Copies the name from the DTO to the entity. If the DTO contains a non-null ID,
     * it is also set on the resulting entity.</p>
     *
     * @param storageRackDto the DTO object to convert
     * @return a {@link StorageRack} entity with values copied from the DTO
     * @throws CoreException if a mapping error occurs during the conversion
     */
    private StorageRack mapToStorageRack(StorageRackDto storageRackDto) throws CoreException {
        StorageRack rack = new StorageRack();
        rack.setName(storageRackDto.getName());
        if (storageRackDto.getStorageRackDtoId() != null) {
            rack.setRackId(storageRackDto.getStorageRackDtoId());
        }
        return rack;
    }

    @Override
    public boolean update(ShelfDto shelfDto) throws CoreException {
        Shelf existingShelf = shelfDao.findById(shelfDto.getShelfDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(SHELF_BY_ID_NOT_FOUND_MESSAGE,
                            shelfDto.getShelfDtoId(), shelfDto.getName()));
                });
        existingShelf.setStorageRack(mapToStorageRack(shelfDto.getStorageRackDto()));
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
    public List<ShelfDto> mapToListObjectsDto(List<Shelf> objectsList)  throws CoreException {
        List<ShelfDto> shelfDtoList = new ArrayList<>();
        for (Shelf shelf : objectsList) {
            shelfDtoList.add(mapToObjectDto(shelf));
        }
        return shelfDtoList;
    }

    @Override
    public ShelfDto mapToObjectDto(Shelf object)  throws CoreException {
        ShelfDto shelfDto = new ShelfDto();

        shelfDto.setShelfDtoId(object.getShelfId());
        shelfDto.setName(object.getName());

        shelfDto.setStorageRackDto(mapToStorageRackDto(object));
        return shelfDto;
    }

    /**
     * Converts the storage rack information contained in the given {@link Shelf}
     * entity into a {@link StorageRackDto}.
     *
     * <p>This method extracts rack ID and name from the {@link Shelf} object’s
     * related rack and maps them into a DTO for transfer or presentation.</p>
     *
     * @param shelf the {@link Shelf} entity that holds the storage rack data
     * @return a {@link StorageRackDto} with rack ID and name
     * @throws CoreException if the shelf or its related storage rack is {@code null}
     */
    private StorageRackDto mapToStorageRackDto(Shelf shelf) throws CoreException {
        StorageRackDto rackDto = new StorageRackDto();
        rackDto.setStorageRackDtoId(shelf.getStorageRack().getRackId());
        rackDto.setName(shelf.getStorageRack().getName());
        return rackDto;
    }
}
