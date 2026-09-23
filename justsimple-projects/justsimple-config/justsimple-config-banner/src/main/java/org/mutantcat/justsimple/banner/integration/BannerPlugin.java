/*
 * Copyright 2017-2025 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mutantcat.justsimple.banner.integration;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;
import org.mutantcat.justsimple.core.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author pmg1991
 * @since 1.11
 * */
public class BannerPlugin implements Plugin {
    static final Logger log = LoggerFactory.getLogger(BannerPlugin.class);

    private String BANNER_DEF_FILE = "META-INF/justsimple_def/banner-def.txt";

	public BannerPlugin() throws IOException {
		boolean enable = JustSimple.cfg().getBool("justsimple.banner.enable", true);

		if (enable) {
			String mode = JustSimple.cfg().get("justsimple.banner.mode", "console");
			String path = JustSimple.cfg().get("justsimple.banner.path", "banner.txt");

			String bannerTxt = ResourceUtil.getResourceAsString(path);
			if (Utils.isEmpty(bannerTxt)) {
				bannerTxt = ResourceUtil.getResourceAsString(BANNER_DEF_FILE);
			}

			//Trying to get the banner file JustSimple
			if (Utils.isEmpty(bannerTxt)) {
				return;
			}

			bannerTxt = bannerTxt.replace("${justsimple.version}", JustSimple.version());

			switch (mode) {
				case "log":
					log.info(bannerTxt);
					break;
				case "both":
					log.info(bannerTxt);
				case "console":
				default:
					System.out.println(bannerTxt);
					break;

			}
		}
	}

	@Override
	public void start(AppContext context) throws Throwable {

	}
}
