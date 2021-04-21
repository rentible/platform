package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.model.CodeStoreItem;
import hu.lanoga.flatshares.repository.CodeStoreItemMapper;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.where.render.WhereClauseProvider;
import org.springframework.stereotype.Service;

import java.sql.JDBCType;
import java.util.List;
import java.util.Map;

@Service
public class CodeStoreItemService implements CrudOperations<CodeStoreItem> {

    private final CodeStoreItemMapper codeStoreItemMapper;

    public CodeStoreItemService(CodeStoreItemMapper codeStoreItemMapper) {
        this.codeStoreItemMapper = codeStoreItemMapper;
    }

    @Override
    public int save(CodeStoreItem entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(CodeStoreItem entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CodeStoreItem findOne(int id) {
        return codeStoreItemMapper.findOne(id);
    }

    public List<CodeStoreItem> findAllBy(int codeStoreTypeId) {
		return codeStoreItemMapper.findAllBy(codeStoreTypeId);
	}

    @Override
    public List<CodeStoreItem> findAll() {
        return codeStoreItemMapper.findAll();
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    public List<CodeStoreItem> findAllWithPaginationAndFilterBy(int offset, int limit, Map filters) {

        WhereClauseProvider whereClause = buildWhereClauseProvider(filters);

        return codeStoreItemMapper.findAllWithPaginationAndFilterBy(offset, limit, whereClause);
    }

    public int count() {
        return codeStoreItemMapper.count();
    }

    public int countByFilters(Map filters) {

        WhereClauseProvider whereClause = buildWhereClauseProvider(filters);

        return codeStoreItemMapper.countBy(whereClause);
    }

    /**
     * Build where clause provider based on filters
     *
     * @param filters
     * @return
     */
    private WhereClauseProvider buildWhereClauseProvider(Map filters) {
        Integer id = null;
        Integer codeStoreTypeId = null;

        if (filters != null && filters.size() > 0) {

            if (filters.get("id") != null) {
                id = Integer.parseInt(String.valueOf(filters.get("id")));
            }

            if (filters.get("codeStoreTypeId") != null) {
                codeStoreTypeId = Integer.parseInt(String.valueOf(filters.get("codeStoreTypeId")));
            }
        }


        final SqlTable codeStoreItemTable = SqlTable.of("public.code_store_item");

        return org.mybatis.dynamic.sql.SqlBuilder
                .where(codeStoreItemTable.column("id", JDBCType.INTEGER), SqlBuilder.isEqualToWhenPresent(id))
                .and(codeStoreItemTable.column("code_store_type_id", JDBCType.INTEGER), SqlBuilder.isEqualToWhenPresent(codeStoreTypeId))
                .build()
                .render(RenderingStrategy.MYBATIS3, "whereClauseProvider");
    }
}
