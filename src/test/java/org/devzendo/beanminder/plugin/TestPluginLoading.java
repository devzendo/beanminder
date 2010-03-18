/**
 * Copyright (C) 2008-2010 Matt Gumbley, DevZendo.org <http://devzendo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devzendo.beanminder.plugin;

import org.devzendo.beanminder.plugin.SimpleAccountsApplicationPlugin;
import org.devzendo.minimiser.pluginmanager.PluginException;
import org.devzendo.minimiser.pluginmanager.PluginHelper;
import org.devzendo.minimiser.pluginmanager.PluginHelperFactory;
import org.junit.Assert;
import org.junit.Test;


/**
 * Tests that this project's plugin is loaded.
 *
 * @author matt
 *
 */
public final class TestPluginLoading {
    /**
     * Load standard plugins, test ours is the application.
     *
     * @throws PluginException
     *         on failure
     */
    @Test
    public void ourPluginIsLoaded() throws PluginException {
        final PluginHelper pluginHelper = PluginHelperFactory.createMiniMiserPluginHelper();
        pluginHelper.loadStandardPlugins();
        Assert.assertTrue(
            "Our plugin is not loaded",
            pluginHelper.getApplicationPlugin() instanceof SimpleAccountsApplicationPlugin);
    }
}
