package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.StorageRackDao;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.entity.StorageRack;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.StorageRackService;
import org.study.hydro.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StorageRackServiceImpl extends EntityMapper<StorageRackDto, StorageRack> implements StorageRackService {

    private final static String STORAGE_RACK_NOT_FOUND_BY_ID_MESSAGE = "Storage rack not found. [id =%s]";
    private final StorageRackDao storageRackDao;

    @Autowired
    public StorageRackServiceImpl(StorageRackDao storageRackDao) {
        this.storageRackDao = storageRackDao;
    }

    @Override
    public boolean create(StorageRackDto storageRackDto) throws CoreException {
        StorageRack storageRack = new StorageRack();
        storageRack.setName(storageRackDto.getName());

        return storageRackDao.create(storageRack) > 0;
    }

    @Override
    public boolean update(StorageRackDto storageRackDto) throws CoreException {
        StorageRack existingStRack = storageRackDao.getStorageRackById(storageRackDto.getStorageRackDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(STORAGE_RACK_NOT_FOUND_BY_ID_MESSAGE,
                            storageRackDto.getStorageRackDtoId()));
                });
        existingStRack.setName(StringUtils.isBlankOrNullText(storageRackDto.getName())
                ? existingStRack.getName() : storageRackDto.getName());
        return storageRackDao.update(existingStRack);
    }

    @Override
    public Optional<StorageRackDto> findById(int id) throws CoreException {
        Optional<StorageRack> storageRack = storageRackDao.getStorageRackById(id);
        if (storageRack.isEmpty()) {
            return Optional.empty();
        }
        return storageRack.map(this::mapToObjectDto);
    }

    @Override
    public List<StorageRackDto> storageRackList(int offset, int limit) throws CoreException {
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
}
