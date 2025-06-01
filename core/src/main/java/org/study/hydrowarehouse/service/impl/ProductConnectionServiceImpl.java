package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductConnectionDao;
import org.study.hydrowarehouse.entity.Dto.ProductConnectionDto;
import org.study.hydrowarehouse.entity.ProductConnection;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ProductConnectionService;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductConnectionServiceImpl extends EntityMapper<ProductConnectionDto, ProductConnection> implements ProductConnectionService {

    private final static String PRODUCT_CONNECTION_BY_ID_NOT_FOUND_MESSAGE = "Product Connection not found. [id = %s.]";

    private final ProductConnectionDao productConnectionDao;

    @Autowired
    public ProductConnectionServiceImpl(ProductConnectionDao productConnectionDao) {
        this.productConnectionDao = productConnectionDao;
    }

    @Override
    public boolean create(ProductConnectionDto productConnectionDto) throws CoreException {
        ProductConnection productConnection = new ProductConnection();
        productConnection.setSize(productConnectionDto.getSize());
        return productConnectionDao.save(productConnection) > 0;
    }

    @Override
    public boolean update(ProductConnectionDto productConnectionDto) throws CoreException {
        ProductConnection existingConnection = productConnectionDao
                .getProductConnectionById(productConnectionDto.getProductConnectionId())
                .orElseThrow(() -> {
                    // logger
                    throw new CoreException(String.format(PRODUCT_CONNECTION_BY_ID_NOT_FOUND_MESSAGE,
                            productConnectionDto.getProductConnectionId()));
                });
        existingConnection.setSize(StringUtils.isBlankOrNullText(productConnectionDto.getSize())
                ? existingConnection.getSize() : productConnectionDto.getSize());

        return productConnectionDao.updateProductConnection(existingConnection);
    }

    @Override
    public List<ProductConnectionDto> findAll() throws CoreException {
        return mapToListObjectsDto(productConnectionDao.getListProductsConnection());
    }

    @Override
    public Optional<ProductConnectionDto> findById(int id) throws CoreException {
        Optional<ProductConnection> productConnection = productConnectionDao.getProductConnectionById(id);
        if (productConnection.isEmpty()) {
            // logger
            return Optional.empty();
        }
        return productConnection.map(this::mapToObjectDto);
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
}
