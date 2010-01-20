package uk.me.gumbley.simpleaccounts.plugin;

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
