package uk.me.gumbley.simpleaccounts.plugin;

import org.junit.Assert;
import org.junit.Test;

import uk.me.gumbley.minimiser.pluginmanager.PluginException;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelper;
import uk.me.gumbley.minimiser.pluginmanager.PluginHelperFactory;

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
