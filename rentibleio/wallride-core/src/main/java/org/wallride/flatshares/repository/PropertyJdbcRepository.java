//package org.wallride.flatshares.repository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.wallride.domain.Property;
//
//import java.util.List;
//
//@Repository
//public class PropertyJdbcRepository {
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    public List<Property> findAll() {
//        return jdbcTemplate.query("SELECT * FROM property", new BeanPropertyRowMapper<>(Property.class));
//    }
//}
