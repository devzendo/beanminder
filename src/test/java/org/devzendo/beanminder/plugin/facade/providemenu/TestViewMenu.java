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

package org.devzendo.beanminder.plugin.facade.providemenu;

import org.devzendo.beanminder.plugin.BeanMinderApplicationPlugin;
import org.devzendo.minimiser.gui.dialog.problem.ProblemReporter;
import org.devzendo.minimiser.gui.dialog.problem.StubProblemReporter;
import org.devzendo.minimiser.gui.menu.ApplicationMenu;
import org.devzendo.minimiser.gui.menu.MenuFacadeImpl;
import org.devzendo.minimiser.gui.menu.MenuProvidingFacadeInitialiser;
import org.devzendo.minimiser.openlist.OpenDatabaseList;
import org.devzendo.minimiser.pluginmanager.PluginException;
import org.devzendo.minimiser.pluginmanager.PluginHelper;
import org.devzendo.minimiser.pluginmanager.PluginHelperFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test that the view menu is correctly initialiased, after a database is opened.
 * It should contain TabIdentifiers for all Accounts.
 * @author matt
 *
 */
public final class TestViewMenu {
    /**
     * @throws PluginException on error
     *
     */
    @Before
    public void getPrerequisites() throws PluginException {
        final OpenDatabaseList openDatabaseList = new OpenDatabaseList();
        final PluginHelper pluginHelper = PluginHelperFactory.createMiniMiserPluginHelper();
        pluginHelper.loadStandardPlugins();
        Assert.assertTrue(
            "Our plugin is not loaded",
            pluginHelper.getApplicationPlugin() instanceof BeanMinderApplicationPlugin);
        final ApplicationMenu globalApplicationMenu = new ApplicationMenu();
        final ProblemReporter problemReporter = new StubProblemReporter();
        new MenuProvidingFacadeInitialiser(pluginHelper.getPluginManager(), openDatabaseList,
            new MenuFacadeImpl(null), globalApplicationMenu, problemReporter);
    }

    /**
     *
     */
    @Test
    public void accountsAreListedInViewMenu() {
        Assert.fail("oh boy, this is going to be one scary integration test");
    }
}
