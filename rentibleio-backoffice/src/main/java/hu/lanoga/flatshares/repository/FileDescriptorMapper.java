package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.FileDescriptor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileDescriptorMapper {

    @Insert("INSERT INTO public.file_descriptor(uuid, original_name, file_path, mime_type, file_size, description, created_on, created_by, modified_on, modified_by) VALUES(#{uuid}, #{originalName}, #{filePath}, #{mimeType}, #{fileSize}, #{description}, #{createdOn}, #{createdBy}, #{modifiedOn}, #{modifiedBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer save(FileDescriptor fileDescriptor);

    @Select("SELECT * FROM public.file_descriptor fd WHERE fd.id = #{id}")
    FileDescriptor findOne(@Param("id") int id);

    @Select("SELECT * FROM public.file_descriptor")
    List<FileDescriptor> findAll();
}
