/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Tag;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

	Tag findOneByIdAndLanguage(Long id, String language);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Tag findOneForUpdateByIdAndLanguage(Long id, String language);

	Tag findOneByNameAndLanguage(String name, String language);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Tag findOneForUpdateByNameAndLanguage(String name, String language);

	List<Tag> findAllByLanguage(String language);

	@Query("select count(tag.id) from Tag tag where tag.language = :language ")
	long count(@Param("language") String language);
}
