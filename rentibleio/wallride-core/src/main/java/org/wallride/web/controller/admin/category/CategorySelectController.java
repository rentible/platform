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

package org.wallride.web.controller.admin.category;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wallride.domain.Category;
import org.wallride.service.CategoryService;
import org.wallride.web.support.DomainObjectSelect2Model;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CategorySelectController {

	@Inject
	private CategoryService categoryService;

	@RequestMapping(value="/{language}/categories/select")
	public @ResponseBody List<DomainObjectSelect2Model> select(
			@PathVariable String language,
			@RequestParam(required=false) String keyword) {
		CategorySearchForm form = new CategorySearchForm();
		form.setKeyword(keyword);
		form.setLanguage(language);
		Page<Category> categories = categoryService.getCategories(form.toCategorySearchRequest());

		List<DomainObjectSelect2Model> results = new ArrayList<>();
		if (categories.hasContent()) {
			for (Category category : categories) {
				DomainObjectSelect2Model model = new DomainObjectSelect2Model(category.getId(), category.getName());
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/categories/select/{id}", method= RequestMethod.GET)
	public @ResponseBody
	DomainObjectSelect2Model select(
			@PathVariable String language,
			@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		Category category = categoryService.getCategoryById(id, language);
		if (category == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelect2Model model = new DomainObjectSelect2Model(category.getId(), category.getName());
		return model;
	}
}
