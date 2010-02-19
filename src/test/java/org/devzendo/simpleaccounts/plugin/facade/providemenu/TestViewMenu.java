package org.devzendo.simpleaccounts.plugin.facade.providemenu;

import org.devzendo.minimiser.openlist.OpenDatabaseList;
import org.devzendo.minimiser.pluginmanager.PluginException;
import org.devzendo.minimiser.pluginmanager.PluginHelper;
import org.devzendo.minimiser.pluginmanager.PluginHelperFactory;
import org.devzendo.simpleaccounts.plugin.SimpleAccountsApplicationPlugin;
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
     * @throws PluginException
     *
     */
    @Before
    public void getPrerequisites() throws PluginException {
        final OpenDatabaseList openDatabaseList = new OpenDatabaseList();
        final PluginHelper pluginHelper = PluginHelperFactory.createMiniMiserPluginHelper();
        pluginHelper.loadStandardPlugins();
        Assert.assertTrue(
            "Our plugin is not loaded",
            pluginHelper.getApplicationPlugin() instanceof SimpleAccountsApplicationPlugin);
//        new MenuProvidingFacadeInitialiser(pluginHelper.getPluginManager(), openDatabaseList,
//            menu, globalApplicationMenu, problemReporter)
    }

    /**
     *
     */
    @Test
    public void accountsAreListedInViewMenu() {
        Assert.fail("oh boy, this is going to be one scary integration test");
    }
}
