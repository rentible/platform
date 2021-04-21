package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.Site;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SiteMapper {

    @Insert("INSERT INTO public.site(country_name, country_2_digit_iso, country_3_digit_iso, created_on, created_by, modified_on, modified_by) VALUES(#{countryName}, #{country2DigitIso}, #{country3DigitIso}, #{createdOn}, #{createdBy}, #{modifiedOn}, #{modifiedBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer save(Site site);

    @Select("SELECT * FROM public.site")
    List<Site> findAll();

    @Select({"SELECT * FROM public.site WHERE id =#{id}"})
    Site findOne(@Param("id") int id);

    @Delete({ "DELETE FROM public.site WHERE id =#{id}" })
    void delete(int id);
}
