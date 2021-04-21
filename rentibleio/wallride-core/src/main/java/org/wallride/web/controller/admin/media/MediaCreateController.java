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

package org.wallride.web.controller.admin.media;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wallride.autoconfigure.WallRideProperties;
import org.wallride.domain.Media;
import org.wallride.service.MediaService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/{language}/media/create")
public class MediaCreateController {

	@Inject
	private MediaService mediaService;
	@Inject
	private WallRideProperties wallRideProperties;

	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody MediaCreatedModel create(@RequestParam MultipartFile file, HttpServletRequest request) {
		Media media = mediaService.createMedia(file);
		return new MediaCreatedModel(media, request.getContextPath(), wallRideProperties);
	}
}
