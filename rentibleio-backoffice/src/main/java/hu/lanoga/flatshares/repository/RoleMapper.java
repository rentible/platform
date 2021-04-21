package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

	@Insert("INSERT INTO public.role(name) VALUES(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	Integer save(Role role);

	@Select("SELECT * FROM public.role")
	List<Role> findAll();

	@Select("SELECT r.* FROM public.role r INNER JOIN public.user_x_role ur ON ur.role_id = r.id INNER JOIN public.USER u ON ur.user_id = u.id AND u.id = #{userId}")
	List<Role> findAllByUserId(@Param("userId") int userId);

	@Select("SELECT * FROM public.role WHERE name = #{name}")
	Role findByName(@Param("name") String name);
}
