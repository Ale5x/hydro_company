package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.ProductConnectionDao;
import org.study.hydro.entity.Dto.ProductConnectionDto;
import org.study.hydro.entity.ProductConnection;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.ProductConnectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductConnectionServiceImpl extends EntityMapper<ProductConnectionDto, ProductConnection> implements ProductConnectionService {

    private final ProductConnectionDao productConnectionDao;

    private final static String PRODUCT_CONNECTION_BY_ID_NOT_FOUND_ERROR = "Product Connection by id not found.";

    @Autowired
    public ProductConnectionServiceImpl(ProductConnectionDao productConnectionDao) {
        this.productConnectionDao = productConnectionDao;
    }

    @Override
    public boolean create(ProductConnectionDto productConnectionDto) throws CoreException {
        return productConnectionDao.save(mapToEntityFromDto(productConnectionDto, false)) > 0;
    }

    @Override
    public boolean update(ProductConnectionDto productConnectionDto) throws CoreException {
        return productConnectionDao.updateProductConnection(mapToEntityFromDto(productConnectionDto, true));
    }

    @Override
    public List<ProductConnectionDto> findAll() throws CoreException {
        return mapToListObjectsDto(productConnectionDao.getListProductsConnection());
    }

    @Override
    public Optional<ProductConnectionDto> findById(int id) throws CoreException {
        return Optional.of(mapToObjectDto(productConnectionDao.getProductConnectionById(id)
                .orElseThrow(() -> new CoreException(PRODUCT_CONNECTION_BY_ID_NOT_FOUND_ERROR))));
    }

    @Override
    public List<ProductConnectionDto> mapToListObjectsDto(List<ProductConnection> objectsList) {
        List<ProductConnectionDto> productConnectionDtoList = new ArrayList<>();
        for (ProductConnection prCon : objectsList) {

            productConnectionDtoList.add(mapToObjectDto(prCon));
        }
        return productConnectionDtoList;
    }

    @Override
    public ProductConnectionDto mapToObjectDto(ProductConnection object) {
        ProductConnectionDto productConnectionDto = new ProductConnectionDto();

        productConnectionDto.setProductConnectionId(object.getProductConnectionId());
        productConnectionDto.setSize(object.getSize());

        return productConnectionDto;
    }

    @Override
    public ProductConnection mapToEntityFromDto(ProductConnectionDto objectDto, boolean isUpdate) {
        ProductConnection productConnection = new ProductConnection();
        if (isUpdate) {
            productConnection.setProductConnectionId(objectDto.getProductConnectionId());
        }

        productConnection.setSize(objectDto.getSize());
        return productConnection;
    }
}
