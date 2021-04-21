package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.where.render.WhereClauseProvider;

import java.util.List;

@Mapper
public interface UserMapper {

	//TODO schema
	@Select({ "SELECT * FROM public.user WHERE username =#{username} LIMIT 1" })
	User findByUsername(@Param("username") String username);

	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
	@Options(useGeneratedKeys = true, keyProperty = "record.id")
	Integer save(InsertStatementProvider<User> insertStatementProvider);

	//TODO schema
	@UpdateProvider(type= SqlProviderAdapter.class, method="update")
	Integer update(UpdateStatementProvider updateStatement);

	@Insert("INSERT INTO public.user_x_role(user_id, role_id) VALUES(#{userId}, #{roleId})")
	Integer addRole(@Param("userId") int userId, @Param("roleId") int roleId);

	//TODO schema
	@Select({"SELECT * FROM public.user WHERE id =#{id}"})
	User findOne(@Param("id") int id);

	@Select("SELECT * from public.user u ${whereClauseProvider.whereClause} ORDER BY id LIMIT #{limit} OFFSET #{offset} ")
	List<User> findAllWithPaginationAndFilterBy(@Param("offset") int offset, @Param("limit") int limit, @Param("whereClauseProvider") WhereClauseProvider whereClause);

	//TODO schema
	@Select({"SELECT * from public.user"})
	List<User> findAll();

	@Select("SELECT count(*) FROM public.user")
	int count();

    @Select("SELECT count(*) FROM public.user u ${whereClauseProvider.whereClause}")
    int countBy(@Param("whereClauseProvider") WhereClauseProvider whereClause);
}
