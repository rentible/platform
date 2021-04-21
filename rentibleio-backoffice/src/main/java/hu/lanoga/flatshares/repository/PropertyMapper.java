package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.Property;
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
public interface PropertyMapper {

	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "record.id")
	Integer save(InsertStatementProvider<Property> insertStatementProvider);

	@UpdateProvider(type = SqlProviderAdapter.class, method = "update")
	Integer update(UpdateStatementProvider updateStatement);

    @Select({"SELECT * FROM ${schema}.property WHERE id =#{id}"})
    Property findOne(@Param("schema") String schema, @Param("id") int id);

	@Select({ "SELECT * FROM public.property" })
	List<Property> findAll();

	//todo FIX IT
//	@Select({ "SELECT p.*, fs.id as fs_id, fs.uuid as fs_uuid, fs.original_name as fs_original_name, fs.file_path as fs_file_path, fs.mime_type as fs_mime_type, fs.file_size as fs_file_size, fs.description as fs_description, fs.created_on as fs_creaton_on, fs.created_by as fs_created_by, fs.modified_on as fs_modified_on, fs.modified_by as fs_modified_by FROM property p INNER JOIN file_descriptor fs ON p.main_image = fs.id WHERE p.created_by =#{id}" })
//	@Results({
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//			@Result(column= "", property = ""),
//
//	})
	@Select({ "SELECT * FROM public.property WHERE user_id =#{id}" })
	List<Property> findByUserId(@Param("id") int id);

    @Delete({"DELETE FROM ${schema}.property WHERE id =#{id}"})
    void delete(@Param("schema") String schema, @Param("id") int id);

    @Select({"SELECT count(*) FROM ${schema}.property p WHERE p.user_id = #{userId}"})
    int countByUser(@Param("schema") String schema, @Param("userId") int userId);

    @Select("SELECT * FROM ${schema}.property WHERE user_id =#{id} ORDER BY id LIMIT #{limit} OFFSET #{offset}")
	List<Property> findByUserIdWithPaginationInTargetSchema(@Param("schema") String schema, @Param("id") int id, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT * FROM public.property ORDER BY id LIMIT #{limit} OFFSET #{offset}")
    List<Property> findAllWithPagination(@Param("offset") int offset, @Param("limit") int limit);
}
