package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.Address;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AddressMapper {

	@Insert("INSERT INTO public.address(address1, address2, address3, city, zip, country, enabled, created_on, created_by) VALUES(#{address1}, #{address2}, #{address3}, #{city}, #{zip}, #{country}, #{enabled}, #{created_on}, #{created_by})")
	// @Options(useGeneratedKeys=true, keyProperty="id") https://stackoverflow.com/questions/4283159/how-to-return-ids-on-inserts-with-mybatis-in-mysql-with-annotations
	@Options(useGeneratedKeys = true, keyProperty = "id")
	Integer save(Address address);

	@Select({ "SELECT * FROM public.address WHERE id =#{id}" })
    Address findOne(@Param("id") int id);

}
