package uk.me.gumbley.simpleaccounts.plugin;

import java.util.List;

import uk.me.gumbley.minimiser.plugin.AbstractPlugin;
import uk.me.gumbley.minimiser.plugin.ApplicationPlugin;
import uk.me.gumbley.minimiser.plugin.facade.newdatabase.NewDatabaseCreation;
import uk.me.gumbley.minimiser.plugin.facade.newdatabase.NewDatabaseCreationFacade;
import uk.me.gumbley.simpleaccounts.plugin.facade.newdatabasecreation.SimpleAccountsNewDatabaseCreationFacade;

public class SimpleAccountsApplicationPlugin extends AbstractPlugin
    implements ApplicationPlugin, NewDatabaseCreation {

    private final SimpleAccountsNewDatabaseCreationFacade mNewDatabaseCreationFacade;

    public SimpleAccountsApplicationPlugin() {
        mNewDatabaseCreationFacade =
            new SimpleAccountsNewDatabaseCreationFacade();
    }
    
    public String getName() {
        return "SimpleAccounts";
    }

    public String getSchemaVersion() {
        return "0.1";
    }

    public String getVersion() {
        return "0.1";
    }

    public NewDatabaseCreationFacade getNewDatabaseCreationFacade() {
        return mNewDatabaseCreationFacade;
    }

    public String getAboutDetailsResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getChangeLogResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDevelopersContactDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFullLicenceDetailsResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIntroPanelBackgroundGraphicResourcePath() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getShortLicenseDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUpdateSiteBaseURL() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<String> getApplicationContextResourcePaths() {
        // TODO Auto-generated method stub
        return null;
    }

    public void shutdown() {
        // TODO Auto-generated method stub
    }
}
