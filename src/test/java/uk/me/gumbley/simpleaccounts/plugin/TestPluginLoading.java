package uk.me.gumbley.simpleaccounts.plugin;

import org.junit.Assert;
import org.junit.Test;

import uk.me.gumbley.minimiser.persistence.PersistencePluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginException;

/**
 * Tests that this project's plugin is loaded.
 * 
 * @author matt
 * 
 */
public final class TestPluginLoading {
    /**
     * Load standard plugins, test ours is the application.
     * @throws PluginException on failure
     */
    @Test
    public void ourPluginIsLoaded() throws PluginException {
        try {
        final PersistencePluginHelper persistencePluginHelper = new PersistencePluginHelper();
        persistencePluginHelper.loadStandardPlugins();
        Assert.assertTrue(
            "Our plugin is not loaded",
            persistencePluginHelper.getApplicationPlugin()
            instanceof SimpleAccountsApplicationPlugin);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
