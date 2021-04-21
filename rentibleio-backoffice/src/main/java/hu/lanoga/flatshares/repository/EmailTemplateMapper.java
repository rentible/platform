package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.EmailTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.dynamic.sql.where.render.WhereClauseProvider;

import java.util.List;

@Mapper
public interface EmailTemplateMapper {

	@Select("SELECT * FROM public.email_template WHERE templ_code=#{templ_code}")
    EmailTemplate findByTemplCode(@Param(value = "templ_code") Integer code);

    @Select({"SELECT * from public.email_template  ${whereClauseProvider.whereClause} ORDER BY id LIMIT #{limit} OFFSET #{offset}"})
    List<EmailTemplate> findAllWithPaginationAndFilterBy(@Param("offset") int offset, @Param("limit") int limit, @Param("whereClauseProvider") WhereClauseProvider whereClause);

    @Select({"SELECT COUNT(*) FROM public.email_template"})
    int count();

    @Select({"SELECT COUNT(*) FROM public.email_template ${whereClauseProvider.whereClause}"})
    int countBy(@Param("whereClauseProvider") WhereClauseProvider whereClause);
}
