package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductSkuStatusDao;
import org.study.hydrowarehouse.entity.Dto.ProductSkuStatusDto;
import org.study.hydrowarehouse.entity.ProductSkuStatus;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ProductSkuStatusService;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link ProductSkuStatusService} interface that handles
 * business logic related to {@link ProductSkuStatus} entities and their DTOs.
 * <p>
 * Uses {@link ProductSkuStatusDao} for persistence operations and
 * {@link EntityMapper} for entity-to-DTO mapping.
 * </p>
 *
 * @author Aliaksandr Pishchala
 * @see EntityMapper
 * @see org.study.hydrowarehouse.service.ProductSkuStatusService
 * @see ProductSkuStatus
 * @see org.study.hydrowarehouse.entity.Dto.ProductSkuDto
 */
@Service
@Transactional
public class ProductSkuStatusServiceImpl extends EntityMapper<ProductSkuStatusDto, ProductSkuStatus> implements ProductSkuStatusService {

    private final ProductSkuStatusDao skuStatusDao;

    @Autowired
    public ProductSkuStatusServiceImpl(ProductSkuStatusDao skuStatusDao) {
        this.skuStatusDao = skuStatusDao;
    }

    @Override
    public boolean save(ProductSkuStatusDto skuStatusDto) throws CoreException {
        return skuStatusDao.save(new ProductSkuStatus(skuStatusDto.getStatus())) > 0;
    }

    @Override
    public boolean update(ProductSkuStatusDto skuStatusDto) throws CoreException {
        Integer id = skuStatusDto.getProductSkuStatusDtoId();
        if (StringUtils.isNullNumericObject(id)) {
            ProductSkuStatus existingSkuStatus = skuStatusDao.findById(id)
                    .orElseThrow(() -> {
                        //logger
                        throw new CoreException(String.format(
                                                    ExceptionMessages.SKU_STATUS_NOT_FOUND_BY_ID_MESSAGE,
                                                    id));
                    });
            existingSkuStatus.setStatus(skuStatusDto.getStatus());
            return skuStatusDao.update(existingSkuStatus);
        }
        return false;
    }

    @Override
    public boolean remove(Integer id) throws CoreException {
        if (StringUtils.isNullNumericObject(id)) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.ID_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_SKU_STATUS_TYPE));
        }
        return skuStatusDao.remove(id);
    }

    @Override
    public List<ProductSkuStatusDto> findAll() throws CoreException {
        return mapToListObjectsDto(skuStatusDao.findAll());
    }

    @Override
    public Optional<ProductSkuStatusDto> findByStatus(String status) throws CoreException {
        Optional<ProductSkuStatus> prSkuStatus = skuStatusDao.findByStatus(status);
        if (prSkuStatus.isEmpty()) {
            return Optional.empty();
        }
        return prSkuStatus.map(this::mapToObjectDto);
    }

    @Override
    public Optional<ProductSkuStatusDto> findById(Integer statusId) throws CoreException {
        if (StringUtils.isNullNumericObject(statusId)) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.ID_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_SKU_STATUS_TYPE));
        }
        Optional<ProductSkuStatus> prSkuStatus = skuStatusDao.findById(statusId);
        if (prSkuStatus.isEmpty()) {
            return Optional.empty();
        }
        return prSkuStatus.map(this::mapToObjectDto);
    }

    @Override
    public List<ProductSkuStatusDto> mapToListObjectsDto(List<ProductSkuStatus> objectsList) throws CoreException {
        if (objectsList == null) return null;
        List<ProductSkuStatusDto> skuStatusDtoList = new ArrayList<>();
        for (ProductSkuStatus skuStatus : objectsList) {
            skuStatusDtoList.add(mapToObjectDto(skuStatus));
        }
        return skuStatusDtoList;
    }

    @Override
    public ProductSkuStatusDto mapToObjectDto(ProductSkuStatus object) throws CoreException {
        if (object == null) return null;
        ProductSkuStatusDto skuDto = new ProductSkuStatusDto();

        skuDto.setProductSkuStatusDtoId(object.getProductSkuStatusId());
        skuDto.setStatus(object.getStatus());
        return skuDto;
    }
}
