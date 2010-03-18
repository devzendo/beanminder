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

package org.devzendo.beanminder.plugin;

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
public class BeanMinderApplicationPlugin extends AbstractPlugin
    implements ApplicationPlugin, NewDatabaseCreation,
    DatabaseOpening, MenuProviding {

    /**
     *
     */
    public BeanMinderApplicationPlugin() {
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
