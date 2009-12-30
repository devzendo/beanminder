package uk.me.gumbley.simpleaccounts.plugin;

import java.util.Arrays;
import java.util.List;

import uk.me.gumbley.minimiser.plugin.AbstractPlugin;
import uk.me.gumbley.minimiser.plugin.ApplicationPlugin;
import uk.me.gumbley.minimiser.plugin.facade.newdatabase.NewDatabaseCreation;
import uk.me.gumbley.minimiser.plugin.facade.newdatabase.NewDatabaseCreationFacade;
import uk.me.gumbley.minimiser.plugin.facade.opendatabase.DatabaseOpening;
import uk.me.gumbley.minimiser.plugin.facade.opendatabase.DatabaseOpeningFacade;

/**
 * The SimpleAccounts application plugin
 * @author matt
 *
 */
public class SimpleAccountsApplicationPlugin extends AbstractPlugin
    implements ApplicationPlugin, NewDatabaseCreation,
    DatabaseOpening {

    /**
     * 
     */
    public SimpleAccountsApplicationPlugin() {
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "SimpleAccounts";
    }

    /**
     * {@inheritDoc}
     */
    public String getSchemaVersion() {
        return "0.1";
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return "0.1";
    }

    /**
     * {@inheritDoc}
     */
    public NewDatabaseCreationFacade getNewDatabaseCreationFacade() {
        return getSpringLoader().getBean("newDatabaseCreationFacade",
            NewDatabaseCreationFacade.class);
    }

    /**
     * {@inheritDoc}
     */
    public DatabaseOpeningFacade getDatabaseOpeningFacade() {
        return getSpringLoader().getBean("databaseOpeningFacade",
            DatabaseOpeningFacade.class);
    }

    /**
     * {@inheritDoc}
     */
    public String getAboutDetailsResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getChangeLogResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getDevelopersContactDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getFullLicenceDetailsResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getIntroPanelBackgroundGraphicResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getShortLicenseDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getUpdateSiteBaseURL() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getApplicationContextResourcePaths() {
        return Arrays.asList(new String[] {
                "uk/me/gumbley/simpleaccounts/SimpleAccounts.xml"
        });
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        // TODO Auto-generated method stub
    }
}
