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

package org.wallride.support;

import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.wallride.domain.Page;
import org.wallride.domain.Post;
import org.wallride.model.PageSearchRequest;
import org.wallride.model.TreeNode;
import org.wallride.service.PageService;
import org.wallride.service.PropertyService;
import org.wallride.service.TemplateService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class PageUtils {

    private PageService pageService;
    private PropertyService propertyService;
    private TemplateService templateService;

    @PersistenceContext
    private EntityManager entityManager;

    public PageUtils(PageService pageService, PropertyService propertyService, TemplateService templateService) {
        this.pageService = pageService;
        this.propertyService = propertyService;
        this.templateService = templateService;
    }

    public List<Page> getAllPages() {
        return getAllPages(false);
    }

    public List<Page> getAllPages(boolean includeUnpublished) {
        PageSearchRequest request = new PageSearchRequest();
        if (!includeUnpublished) {
            request.setStatus(Post.Status.PUBLISHED);
        }
        return pageService.getPages(request).getContent();
    }

    public List<TreeNode<Page>> getNodes() {
        return getNodes(false);
    }

    public List<TreeNode<Page>> getNodes(boolean includeUnpublished) {
        PageSearchRequest request = new PageSearchRequest();
        if (!includeUnpublished) {
            request.setStatus(Post.Status.PUBLISHED);
        }
        Collection<Page> pages = new TreeSet<>(pageService.getPages(request).getContent());

        List<TreeNode<Page>> rootNodes = new ArrayList<>();
        Iterator<Page> i = pages.iterator();

        while (i.hasNext()) {

            Page page = i.next();

            if (page.getBody().contains(CommandUtils.COMMAND_BEGIN)) {
                entityManager.detach(page);
                int idx1 = page.getBody().indexOf(CommandUtils.COMMAND_BEGIN);
                int idx2 = page.getBody().indexOf(CommandUtils.COMMAND_END);

                String preCommand = page.getBody().substring(idx1, idx2 +CommandUtils.COMMAND_END.length());
                String command = page.getBody().substring(idx1 + CommandUtils.COMMAND_BEGIN.length(), idx2);

                String[] cmd = command.split(",");

                page.setBody(page.getBody().replace(preCommand, CommandUtils.getHtml(cmd[0], propertyService, templateService)));
            }

            if (page.getParent() == null) {
                TreeNode<Page> node = new TreeNode<>(page);
                rootNodes.add(node);
                i.remove();
            }
        }

        for (TreeNode<Page> node : rootNodes) {
            createNode(node, pages);
        }
        return rootNodes;
    }

    private void createNode(TreeNode<Page> parent, Collection<Page> pages) {
        List<TreeNode<Page>> children = new ArrayList<>();
        Iterator<Page> i = pages.iterator();
        while (i.hasNext()) {
            Page page = i.next();
            TreeNode<Page> node = new TreeNode<>(page);
            node.setParent(parent);
            if (parent.getObject().equals(page.getParent())) {
                children.add(node);
                i.remove();
            }
        }
        parent.setChildren(children);

        for (TreeNode<Page> node : children) {
            createNode(node, pages);
        }
    }

    public Map<Page, String> getPaths(Page page) {
        return getPaths(page, false);
    }

    public Map<Page, String> getPaths(Page page, boolean includeUnpublished) {
        List<Page> parents = pageService.getPathPages(page, includeUnpublished);
        Map<Page, String> paths = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(parents)) {
            return paths;
        }
        StringBuilder path = new StringBuilder();
        for (Page p : parents) {
            if (path != null) {
                path.append("/");
			}
            path.append(p.getCode());
            paths.put(p, path.toString());
        }
        return paths;
    }

    public List<Page> getChildren(Page page) {
        return getChildren(page, false);
    }

    public List<Page> getChildren(Page page, boolean includeUnpublished) {
        return pageService.getChildPages(page, includeUnpublished);
    }

    public List<Page> getSiblings(Page page) {
        return getSiblings(page, false);
    }

    public List<Page> getSiblings(Page page, boolean includeUnpublished) {
        return pageService.getSiblingPages(page, includeUnpublished);
    }

    public org.springframework.data.domain.Page<Page> search(PageSearchRequest request, int size) {
        return pageService.getPages(request, new PageRequest(0, size));
    }
}
