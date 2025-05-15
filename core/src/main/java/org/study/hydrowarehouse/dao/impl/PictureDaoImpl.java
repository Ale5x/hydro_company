package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.PictureDao;
import org.study.hydrowarehouse.entity.Picture;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(rollbackFor = Exception.class)
public class PictureDaoImpl extends CriteriaQueryHelper<Picture> implements PictureDao {

    private final static String PICTURE_ID = "pictureId";
    private final static String PRODUCT_ID = "productId";
    private final static String DELETE_PICTURE_PATH_QUERY = String.format("DELETE Picture WHERE id =: %s", PICTURE_ID);
    private final static String GET_PICTURES_BY_PRODUCT_ID = "SELECT pictures FROM Picture AS pictures " +
            "JOIN pictures.product AS picture WHERE pictures.product.productId =: productId";

    @Override
    public int create(Picture picture) {
        Session session = getCurrentSession();
        session.save(picture);
        session.flush();
        return picture.getPictureId();
    }

    @Override
    public boolean update(Picture picture) {
        Session session = getCurrentSession();
        session.saveOrUpdate(picture);
        return true;
    }

    @Override
    public boolean createList(List<Picture> pictures) {
        Session session = getCurrentSession();

        for (Picture picture : pictures) {
            session.persist(picture);
        }
        session.flush();

        return pictures.get(0).getPictureId() > 0;
    }

    @Override
    public boolean remove(Integer id) {
        Session session = getCurrentSession();

        return session.createQuery(DELETE_PICTURE_PATH_QUERY)
                .setParameter(PICTURE_ID, id)
                .executeUpdate() > 0;
    }

    @Override
    public List<Picture> getPicturesByProductId(Integer productId) {
        Session session = getCurrentSession();
        return session.createQuery(GET_PICTURES_BY_PRODUCT_ID, Picture.class)
                    .setParameter(PRODUCT_ID, productId)
                    .getResultList();
    }

    @Override
    public List<Picture> getPictures(int limit, int offset) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Picture> criteriaQuery = getCriteriaQuery(criteriaBuilder, Picture.class);
        Root<Picture> pictureRoot = getRoot(criteriaQuery, Picture.class);

        criteriaQuery.select(pictureRoot);
        return session.createQuery(criteriaQuery)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
    }

    @Override
    public Optional<Picture> findById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Picture> criteriaQuery = getCriteriaQuery(criteriaBuilder, Picture.class);
        Root<Picture> pictureRoot = getRoot(criteriaQuery, Picture.class);

        criteriaQuery.select(pictureRoot)
                .where(criteriaBuilder.equal(pictureRoot.get(PICTURE_ID), id));
        return session.createQuery(criteriaQuery)
                        .getResultList()
                        .stream()
                        .findFirst();
    }
}