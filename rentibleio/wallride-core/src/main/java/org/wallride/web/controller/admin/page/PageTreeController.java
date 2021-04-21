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

package org.wallride.web.controller.admin.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.service.PageService;
import org.wallride.support.PageUtils;

import javax.inject.Inject;

@Controller
@RequestMapping("/{language}/pages/tree")
public class PageTreeController {

	@Inject
	private PageService pageService;

	@Inject
	private PageUtils pageUtils;

	@ModelAttribute("form")
	public PageCreateForm pageCreateForm() {
		return new PageCreateForm();
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping
	public String index(@PathVariable String language, Model model) {
		model.addAttribute("pageNodes", pageUtils.getNodes(true));
		return "page/tree";
	}

	@RequestMapping(params="part=page-create-form")
	public String partPageCreateDialog(@PathVariable String language, @RequestParam(required=false) Long parentId, Model model) {
		model.addAttribute("parentId", parentId);
		model.addAttribute("pageNodes", pageUtils.getNodes(true));
		return "page/tree::page-create-form";
	}

	@RequestMapping(params="part=page-edit-form")
	public String partPageEditDialog(@PathVariable String language, @RequestParam long id, Model model) {
		model.addAttribute("pageNodes", pageUtils.getNodes(true));
		model.addAttribute("page", pageService.getPageById(id, language));
		return "page/tree::page-edit-form";
	}

	@RequestMapping(params="part=page-delete-form")
	public String partPageDeleteDialog(@RequestParam long id, Model model) {
		model.addAttribute("targetId", id);
		return "page/tree::page-delete-form";
	}
}