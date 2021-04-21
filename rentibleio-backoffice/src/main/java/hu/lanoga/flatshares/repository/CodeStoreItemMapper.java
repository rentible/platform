package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.CodeStoreItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.dynamic.sql.where.render.WhereClauseProvider;

import java.util.List;

@Mapper
public interface CodeStoreItemMapper {

    @Select({"SELECT * FROM public.code_store_item"})
    List<CodeStoreItem> findAll();

    @Select({"SELECT * FROM public.code_store_item WHERE code_store_type_id = #{codeStoreTypeId}"})
    List<CodeStoreItem> findAllBy(@Param("codeStoreTypeId") int codeStoreTypeId);

    @Select({"SELECT * FROM public.code_store_item WHERE id =#{id}"})
    CodeStoreItem findOne(@Param("id") int id);

    @Select({"SELECT * from public.code_store_item  ${whereClauseProvider.whereClause} ORDER BY id LIMIT #{limit} OFFSET #{offset}"})
    List<CodeStoreItem> findAllWithPaginationAndFilterBy(@Param("offset") int offset, @Param("limit") int limit, @Param("whereClauseProvider") WhereClauseProvider whereClause);

    @Select({"SELECT COUNT(*) FROM public.code_store_item"})
    int count();

    @Select({"SELECT COUNT(*) FROM public.code_store_item ${whereClauseProvider.whereClause}"})
    int countBy(@Param("whereClauseProvider") WhereClauseProvider whereClause);
}
