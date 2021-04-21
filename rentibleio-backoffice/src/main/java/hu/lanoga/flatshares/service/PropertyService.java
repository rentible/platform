package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.exception.MissingPropertyException;
import hu.lanoga.flatshares.model.Property;
import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.repository.FileDescriptorMapper;
import hu.lanoga.flatshares.repository.PropertyMapper;
import hu.lanoga.flatshares.util.ObjectUtil;
import org.apache.commons.lang.StringUtils;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PropertyService {

    private final PropertyMapper propertyMapper;
    private final FileDescriptorMapper fileDescriptorMapper;

    public PropertyService(PropertyMapper propertyMapper, FileDescriptorMapper fileDescriptorMapper) {
        this.propertyMapper = propertyMapper;
        this.fileDescriptorMapper = fileDescriptorMapper;
    }

    /**
     * Save a property into the target schema
     *
     * @param schema   target schema
     * @param property given property
     * @return number of created records - it has to be zero or one
     */
    public int save(String schema, Property property) {

        if (StringUtils.isNotBlank(schema)) {
            final SqlTable propertyTable = SqlTable.of(schema + ".property");

            InsertStatementProvider<Property> insertStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.insert(property)
                    .into(propertyTable)
                    .map(propertyTable.column("ad_id", JDBCType.INTEGER)).toPropertyWhenPresent("adId", property::getAdId)
                    .map(propertyTable.column("rental_type", JDBCType.INTEGER)).toPropertyWhenPresent("rentalType", property::getRentalType)
                    .map(propertyTable.column("cross_street1", JDBCType.VARCHAR)).toPropertyWhenPresent("crossStreet1", property::getCrossStreet1)
                    .map(propertyTable.column("cross_street2", JDBCType.VARCHAR)).toPropertyWhenPresent("crossStreet2", property::getCrossStreet2)
                    .map(propertyTable.column("property_type", JDBCType.INTEGER)).toPropertyWhenPresent("propertyType", property::getPropertyType)
                    .map(propertyTable.column("reference_id", JDBCType.VARCHAR)).toPropertyWhenPresent("referenceId", property::getReferenceId)
                    .map(propertyTable.column("rent_price", JDBCType.NUMERIC)).toPropertyWhenPresent("rentPrice", property::getRentPrice)
                    .map(propertyTable.column("rent_price_currency", JDBCType.INTEGER)).toPropertyWhenPresent("rentPriceCurrency", property::getRentPriceCurrency)
                    .map(propertyTable.column("deposit", JDBCType.VARCHAR)).toPropertyWhenPresent("deposit", property::getDeposit)
                    .map(propertyTable.column("deposit_currency", JDBCType.INTEGER)).toPropertyWhenPresent("depositCurrency", property::getDepositCurrency)
                    .map(propertyTable.column("apartment_size", JDBCType.INTEGER)).toPropertyWhenPresent("apartmentSize", property::getApartmentSize)
                    .map(propertyTable.column("room_size", JDBCType.VARCHAR)).toPropertyWhenPresent("roomSize", property::getRoomSize)
                    .map(propertyTable.column("bedroom", JDBCType.INTEGER)).toPropertyWhenPresent("bedroom", property::getBedroom)
                    .map(propertyTable.column("room", JDBCType.VARCHAR)).toPropertyWhenPresent("room", property::getRoom)
                    .map(propertyTable.column("bathroom", JDBCType.VARCHAR)).toPropertyWhenPresent("bathroom", property::getBathroom)
                    .map(propertyTable.column("toilet", JDBCType.INTEGER)).toPropertyWhenPresent("toilet", property::getToilet)
                    .map(propertyTable.column("furnished", JDBCType.BOOLEAN)).toPropertyWhenPresent("furnished", property::getFurnished)
                    .map(propertyTable.column("termoflease", JDBCType.VARCHAR)).toPropertyWhenPresent("termoflease", property::getTermoflease)
                    .map(propertyTable.column("available_from", JDBCType.TIMESTAMP)).toPropertyWhenPresent("availableFrom", property::getAvailableFrom)
                    .map(propertyTable.column("apartment_condition", JDBCType.INTEGER)).toPropertyWhenPresent("apartmentCondition", property::getApartmentCondition)
                    .map(propertyTable.column("view", JDBCType.INTEGER)).toPropertyWhenPresent("view", property::getView)
                    .map(propertyTable.column("floor", JDBCType.INTEGER)).toPropertyWhenPresent("floor", property::getView)
                    .map(propertyTable.column("heating", JDBCType.INTEGER)).toPropertyWhenPresent("heating", property::getHeating)
                    .map(propertyTable.column("building_condition", JDBCType.INTEGER)).toPropertyWhenPresent("buildingCondition", property::getBuildingCondition)
                    .map(propertyTable.column("year_built", JDBCType.INTEGER)).toPropertyWhenPresent("yearBuilt", property::getYearBuilt)
                    .map(propertyTable.column("common_cost", JDBCType.NUMERIC)).toPropertyWhenPresent("commonCost", property::getCommonCost)
                    .map(propertyTable.column("heating_included", JDBCType.BOOLEAN)).toPropertyWhenPresent("heatingIncluded", property::getHeatingIncluded)
                    .map(propertyTable.column("near_to_metro", JDBCType.VARCHAR)).toPropertyWhenPresent("nearToMetro", property::getNearToMetro)
                    .map(propertyTable.column("distance_to_public_transport", JDBCType.INTEGER)).toPropertyWhenPresent("distanceToPublicTransport", property::getDistanceToPublicTransport)
                    .map(propertyTable.column("surrounding", JDBCType.VARCHAR)).toPropertyWhenPresent("surrounding", property::getSurrounding)
                    .map(propertyTable.column("amenity", JDBCType.VARCHAR)).toPropertyWhenPresent("amenity", property::getAmenity)
                    .map(propertyTable.column("elevator", JDBCType.BOOLEAN)).toPropertyWhenPresent("elevator", property::getElevator)
                    .map(propertyTable.column("location_lat", JDBCType.REAL)).toPropertyWhenPresent("locationLat", property::getLocationLat)
                    .map(propertyTable.column("location_lng", JDBCType.REAL)).toPropertyWhenPresent("locationLng", property::getLocationLng)
                    .map(propertyTable.column("address_id", JDBCType.INTEGER)).toPropertyWhenPresent("addressId", property::getAddressId)
                    .map(propertyTable.column("title", JDBCType.VARCHAR)).toPropertyWhenPresent("title", property::getTitle)
                    .map(propertyTable.column("main_image", JDBCType.INTEGER)).toPropertyWhenPresent("mainImage", property::getMainImage)
                    .map(propertyTable.column("description", JDBCType.VARCHAR)).toPropertyWhenPresent("description", property::getDescription)
                    .map(propertyTable.column("user_id", JDBCType.INTEGER)).toPropertyWhenPresent("userId", property::getUserId)
                    .map(propertyTable.column("enabled", JDBCType.BOOLEAN)).toPropertyWhenPresent("enabled", property::getEnabled)
                    .map(propertyTable.column("created_on", JDBCType.TIMESTAMP)).toPropertyWhenPresent("createdOn", property::getCreatedOn)
                    .map(propertyTable.column("created_by", JDBCType.INTEGER)).toPropertyWhenPresent("createdBy", property::getCreatedBy)
                    .map(propertyTable.column("modified_on", JDBCType.TIMESTAMP)).toPropertyWhenPresent("modifiedOn", property::getModifiedOn)
                    .map(propertyTable.column("modified_by", JDBCType.INTEGER)).toPropertyWhenPresent("modifiedBy", property::getModifiedBy)
                    .build()
                    .render(RenderingStrategy.MYBATIS3);
            return propertyMapper.save(insertStatementProvider);
        } else {
            throw new MissingPropertyException("Missing schema name during saving a property!");
        }
    }

    /**
     * Update a property in the target schema by id
     *
     * @param schema   target schema
     * @param property given property
     * @return number of created records - it has to be zero or one
     */

    public int update(String schema, Property property) {

        if (StringUtils.isNotBlank(schema)) {
            if (ObjectUtil.checkAllFieldNullOrEmpty(property)) {
                final SqlTable propertyTable = SqlTable.of(schema + ".property");

                UpdateStatementProvider updateStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.update(propertyTable)
                        .set(propertyTable.column("ad_id", JDBCType.INTEGER)).equalToWhenPresent(property.getAdId())
                        .set(propertyTable.column("rental_type", JDBCType.INTEGER)).equalToWhenPresent(property.getRentalType())
                        .set(propertyTable.column("cross_street1", JDBCType.VARCHAR)).equalToWhenPresent(property.getCrossStreet1())
                        .set(propertyTable.column("cross_street2", JDBCType.VARCHAR)).equalToWhenPresent(property.getCrossStreet2())
                        .set(propertyTable.column("property_type", JDBCType.INTEGER)).equalToWhenPresent(property.getPropertyType())
                        .set(propertyTable.column("reference_id", JDBCType.VARCHAR)).equalToWhenPresent(property.getReferenceId())
                        .set(propertyTable.column("rent_price", JDBCType.NUMERIC)).equalToWhenPresent(property.getRentPrice())
                        .set(propertyTable.column("rent_price_currency", JDBCType.VARCHAR)).equalToWhenPresent(property.getRentPriceCurrency())
                        .set(propertyTable.column("deposit", JDBCType.VARCHAR)).equalToWhenPresent(property.getDeposit())
                        .set(propertyTable.column("deposit_currency", JDBCType.VARCHAR)).equalToWhenPresent(property.getDepositCurrency())
                        .set(propertyTable.column("apartment_size", JDBCType.INTEGER)).equalToWhenPresent(property.getApartmentSize())
                        .set(propertyTable.column("room_size", JDBCType.VARCHAR)).equalToWhenPresent(property.getRoomSize())
                        .set(propertyTable.column("bedroom", JDBCType.INTEGER)).equalToWhenPresent(property.getBedroom())
                        .set(propertyTable.column("room", JDBCType.VARCHAR)).equalToWhenPresent(property.getRoom())
                        .set(propertyTable.column("bathroom", JDBCType.VARCHAR)).equalToWhenPresent(property.getBathroom())
                        .set(propertyTable.column("toilet", JDBCType.INTEGER)).equalToWhenPresent(property.getToilet())
                        .set(propertyTable.column("furnished", JDBCType.BOOLEAN)).equalToWhenPresent(property.getFurnished())
                        .set(propertyTable.column("termoflease", JDBCType.VARCHAR)).equalToWhenPresent(property.getTermoflease())
                        .set(propertyTable.column("available_from", JDBCType.TIMESTAMP)).equalToWhenPresent(property.getAvailableFrom())
                        .set(propertyTable.column("apartment_condition", JDBCType.INTEGER)).equalToWhenPresent(property.getApartmentCondition())
                        .set(propertyTable.column("view", JDBCType.INTEGER)).equalToWhenPresent(property.getView())
                        .set(propertyTable.column("floor", JDBCType.INTEGER)).equalToWhenPresent(property.getFloor())
                        .set(propertyTable.column("heating", JDBCType.INTEGER)).equalToWhenPresent(property.getHeating())
                        .set(propertyTable.column("building_condition", JDBCType.INTEGER)).equalToWhenPresent(property.getBuildingCondition())
                        .set(propertyTable.column("year_built", JDBCType.INTEGER)).equalToWhenPresent(property.getYearBuilt())
                        .set(propertyTable.column("common_cost", JDBCType.NUMERIC)).equalToWhenPresent(property.getCommonCost())
                        .set(propertyTable.column("heating_included", JDBCType.BOOLEAN)).equalToWhenPresent(property.getHeatingIncluded())
                        .set(propertyTable.column("near_to_metro", JDBCType.VARCHAR)).equalToWhenPresent(property.getNearToMetro())
                        .set(propertyTable.column("distance_to_public_transport", JDBCType.INTEGER)).equalToWhenPresent(property.getDistanceToPublicTransport())
                        .set(propertyTable.column("surrounding", JDBCType.VARCHAR)).equalToWhenPresent(property.getSurrounding())
                        .set(propertyTable.column("amenity", JDBCType.VARCHAR)).equalToWhenPresent(property.getAmenity())
                        .set(propertyTable.column("elevator", JDBCType.BOOLEAN)).equalToWhenPresent(property.getElevator())
                        .set(propertyTable.column("location_lat", JDBCType.REAL)).equalToWhenPresent(property.getLocationLat())
                        .set(propertyTable.column("location_lng", JDBCType.REAL)).equalToWhenPresent(property.getLocationLng())
                        .set(propertyTable.column("address_id", JDBCType.INTEGER)).equalToWhenPresent(property.getAddressId())
                        .set(propertyTable.column("title", JDBCType.VARCHAR)).equalToWhenPresent(property.getTitle())
                        .set(propertyTable.column("main_image", JDBCType.INTEGER)).equalToWhenPresent(property.getMainImage())
                        .set(propertyTable.column("description", JDBCType.VARCHAR)).equalToWhenPresent(property.getDescription())
                        .set(propertyTable.column("enabled", JDBCType.BOOLEAN)).equalToWhenPresent(property.getEnabled())
                        //				.set(propertyTable.column("type", JDBCType.VARCHAR)).equalToWhenPresent()
                        .set(propertyTable.column("modified_on", JDBCType.TIMESTAMP)).equalToWhenPresent(property.getModifiedOn())
                        .set(propertyTable.column("modified_by", JDBCType.INTEGER)).equalToWhenPresent(property.getModifiedBy())
                        .where(propertyTable.column("id", JDBCType.INTEGER), org.mybatis.dynamic.sql.SqlBuilder.isEqualTo(property.getId()))
                        .build()
                        .render(RenderingStrategy.MYBATIS3);

                return propertyMapper.update(updateStatementProvider);
            } else {
                throw new MissingPropertyException("Missing values during saving a property!");
            }

        } else {
            throw new MissingPropertyException("Missing schema name during saving a property!");
        }

    }

    /**
     * Find a property in the target schema by id
     *
     * @param schema target schema
     * @param id     property id
     * @return zero or one property
     */
    public Property findOne(String schema, int id) {
        return propertyMapper.findOne(schema, id);
    }

    public List<Property> findAll() {
        List<Property> properties = propertyMapper.findAll();
        for (Property p : properties) {
            if (p.getMainImage() != null) {
                p.setMainImageObject(fileDescriptorMapper.findOne(p.getMainImage()));
            }
        }

        return properties;
    }

    public void delete(String schema, int id) {
        propertyMapper.delete(schema, id);
    }

    public List<Property> findAllBy(int userId) {
        //FIXME with a proper mybatis annotation based solution
        List<Property> properties = propertyMapper.findByUserId(userId);
        for (Property p : properties) {
            if (p.getMainImage() != null) {
                p.setMainImageObject(fileDescriptorMapper.findOne(p.getMainImage()));
            }
        }

        return properties;
    }

    //TODO implement

    /**
     * Filter properties by user and country
     * @param userId userId
     * @param countryCode countryCode
     * @return Filtered list of properties.
     */
    public List<Property> findAllBy(int userId, String countryCode) {
        return findAllBy(userId);
    }

    public List findAllWithPagination(int offset, int limit) {
        return propertyMapper.findAllWithPagination(offset, limit);
    }

    public List findByUserWithPagination(User user, int offset, int limit) {
        return propertyMapper.findByUserIdWithPaginationInTargetSchema(user.getSchema(), user.getId(), offset, limit);
	}

    //TODO
    public List<String> getAvailableCountryCodeByUserId(int id) {
        return Arrays.asList("HU","GB", "FR");
    }

    /**
     *
     * @param user
     * @return
     */
    public int countByUser(User user) {
        return propertyMapper.countByUser(user.getSchema(), user.getId());
    }
}
