package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.StorageRackDao;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.entity.StorageRack;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.ShelfService;
import org.study.hydro.service.StorageRackService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StorageRackServiceImpl extends EntityMapper<StorageRackDto, StorageRack> implements StorageRackService {

    private final static String NOT_FOUND_STORAGE_RACK_ERROR = "Storage rack not found";
    private final static String SHELF_FOR_STORAGE_NOT_FOUND_ERROR = "Shelf for storage rack not found by name.";
    private final StorageRackDao storageRackDao;
    private final ShelfService shelfService;

    @Autowired
    public StorageRackServiceImpl(StorageRackDao storageRackDao, ShelfService shelfService) {
        this.storageRackDao = storageRackDao;
        this.shelfService = shelfService;
    }

    @Override
    public boolean create(StorageRackDto storageRackDto) throws CoreException {
        return storageRackDao.create(mapToEntityFromDto(storageRackDto, false));
    }

    @Override
    public boolean update(StorageRackDto storageRackDto) throws CoreException {
        return storageRackDao.update(mapToEntityFromDto(storageRackDto, true));
    }

    @Override
    public Optional<StorageRackDto> findById(int id) throws CoreException {
        return Optional.of(mapToObjectDto(
                storageRackDao.getStorageRackById(id)
                        .orElseThrow(() -> new CoreException(NOT_FOUND_STORAGE_RACK_ERROR))));
    }

    @Override
    public List<StorageRackDto> storageRackList(int limit, int offset) throws CoreException {
        return mapToListObjectsDto(storageRackDao.getStorageRacksList(limit, offset));
    }


    @Override
    public List<StorageRackDto> mapToListObjectsDto(List<StorageRack> objectsList) {
        List<StorageRackDto> stRackDtoList = new ArrayList<>();
        for (StorageRack storageRack: objectsList) {

            stRackDtoList.add(mapToObjectDto(storageRack));
        }
        return stRackDtoList;
    }

    @Override
    public StorageRackDto mapToObjectDto(StorageRack object) {
        StorageRackDto storageRackDto = new StorageRackDto();
        storageRackDto.setStorageRackDtoId(object.getStorageRackId());
        storageRackDto.setName(object.getName());
        storageRackDto.setShelfName(object.getShelf().getName());

        return storageRackDto;
    }


    @Override
    public StorageRack mapToEntityFromDto(StorageRackDto objectDto, boolean isUpdate) {
        StorageRack storageRack = new StorageRack();

        if (isUpdate) {
            storageRack.setStorageRackId(objectDto.getStorageRackDtoId());

        }
        System.out.println("objectDto.getName() -> " + objectDto.getName());
        storageRack.setName(objectDto.getName());
        storageRack.setShelf(shelfService.findShelfByName(objectDto.getShelfName())
                .orElseThrow(() -> new CoreException(SHELF_FOR_STORAGE_NOT_FOUND_ERROR)));
        System.out.println("storageRack -> " + storageRack);
        return storageRack;
    }
}
