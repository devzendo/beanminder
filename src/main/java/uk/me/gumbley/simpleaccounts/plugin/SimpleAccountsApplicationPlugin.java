package uk.me.gumbley.simpleaccounts.plugin;

import java.util.Arrays;
import java.util.List;

import uk.me.gumbley.minimiser.plugin.AbstractPlugin;
import uk.me.gumbley.minimiser.plugin.ApplicationPlugin;
import uk.me.gumbley.minimiser.plugin.facade.newdatabase.NewDatabaseCreation;
import uk.me.gumbley.minimiser.plugin.facade.newdatabase.NewDatabaseCreationFacade;

public class SimpleAccountsApplicationPlugin extends AbstractPlugin
    implements ApplicationPlugin, NewDatabaseCreation {

    public SimpleAccountsApplicationPlugin() {
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
        return getSpringLoader().getBean("newDatabaseCreationFacade",
            NewDatabaseCreationFacade.class);
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
        return Arrays.asList(new String[] {
                "uk/me/gumbley/simpleaccounts/SimpleAccounts.xml"
        });
    }

    public void shutdown() {
        // TODO Auto-generated method stub
    }
}
