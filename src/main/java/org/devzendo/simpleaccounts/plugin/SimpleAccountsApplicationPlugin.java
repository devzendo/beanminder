package org.devzendo.simpleaccounts.plugin;

import java.util.Arrays;
import java.util.List;

import org.devzendo.minimiser.plugin.AbstractPlugin;
import org.devzendo.minimiser.plugin.ApplicationPlugin;
import org.devzendo.minimiser.plugin.facade.newdatabase.NewDatabaseCreation;
import org.devzendo.minimiser.plugin.facade.newdatabase.NewDatabaseCreationFacade;
import org.devzendo.minimiser.plugin.facade.opendatabase.DatabaseOpening;
import org.devzendo.minimiser.plugin.facade.opendatabase.DatabaseOpeningFacade;
import org.devzendo.minimiser.plugin.facade.providemenu.MenuProviding;
import org.devzendo.minimiser.plugin.facade.providemenu.MenuProvidingFacade;


/**
 * The SimpleAccounts application plugin
 * @author matt
 *
 */
public class SimpleAccountsApplicationPlugin extends AbstractPlugin
    implements ApplicationPlugin, NewDatabaseCreation,
    DatabaseOpening, MenuProviding {

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
    public MenuProvidingFacade getMenuProvidingFacade() {
        return getSpringLoader().getBean("menuProvidingFacade",
            MenuProvidingFacade.class);
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
                "org/devzendo/simpleaccounts/SimpleAccounts.xml"
        });
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        // TODO Auto-generated method stub
    }
}
