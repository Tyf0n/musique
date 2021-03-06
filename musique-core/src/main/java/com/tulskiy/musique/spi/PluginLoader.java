/*
 * Copyright (c) 2008, 2009, 2010, 2011 Denis Tulskiy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tulskiy.musique.spi;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Denis Tulskiy
 * Date: 2/27/11
 */
public class PluginLoader {
    private static final Logger logger = Logger.getLogger("musique");

    public void load() {
        try {
            logger.fine("Loading plugins");
            URLClassLoader classLoader = new URLClassLoader(new URL[]{
                    new File("musique.jar").toURI().toURL(),
            });
            ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class, classLoader);
            for (Plugin plugin : loader) {
                try {
                    System.out.println(plugin);
                    plugin.init();
                } catch (Exception e) {
                    logger.log(Level.FINE, "Error loading " + plugin.getDescription(), e);
                }
            }
            logger.fine("Finished loading plugins");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
