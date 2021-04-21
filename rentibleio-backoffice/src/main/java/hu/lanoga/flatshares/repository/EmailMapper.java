package hu.lanoga.flatshares.repository;

import hu.lanoga.flatshares.model.Email;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmailMapper {

	@Insert("INSERT INTO public.email(from_email, to_email, subject, body, is_plain_text, success, attempt) VALUES(#{fromEmail}, #{toEmail}, #{subject}, #{body}, #{isPlainText}, #{success}, #{attempt})")
	Integer save(Email email);

	@Update("UPDATE public.email SET from_email=#{fromEmail}, to_email=#{toEmail}, subject=#{subject}, body=#{body}, is_plain_text = #{isPlainText}, success = #{success}, attempt = #{attempt} WHERE id =#{id}")
	Integer update(Email email);

	@Select("SELECT * FROM public.email WHERE success = false AND attempt < 5")
	List<Email> findAllToScheduler();
}
