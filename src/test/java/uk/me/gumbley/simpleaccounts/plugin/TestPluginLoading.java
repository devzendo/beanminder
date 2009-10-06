package uk.me.gumbley.simpleaccounts.plugin;

import org.junit.Assert;
import org.junit.Test;

import uk.me.gumbley.minimiser.pluginmanager.PluginException;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelper;

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
        try {
            final PluginHelper pluginHelper = new PluginHelper(false);
            pluginHelper.loadStandardPlugins();
            Assert.assertTrue(
                "Our plugin is not loaded",
                pluginHelper.getApplicationPlugin() instanceof SimpleAccountsApplicationPlugin);
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }
}
