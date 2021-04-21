package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.UserDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import java.util.List;

@Mapper
public interface UserDetailMapper {

    @Select("SELECT * FROM ${schema}.user_detail")
    List<UserDetail> findAll(@Param("schema") String schema);

    @Select("SELECT * FROM ${schema}.user_detail WHERE user_id = #{userId}")
    UserDetail findOne(@Param("schema") String schema, @Param("userId") int userId);

    @Delete("DELETE FROM ${schema}.user_detail WHERE id = #{id}")
    int delete(@Param("schema") String schema, @Param("id") int id);

    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    Integer update(UpdateStatementProvider updateStatement);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "record.id")
    Integer save(InsertStatementProvider<UserDetail> insertStatementProvider);
}
