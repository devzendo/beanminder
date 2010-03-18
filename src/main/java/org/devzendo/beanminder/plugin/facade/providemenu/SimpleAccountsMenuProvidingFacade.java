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

package org.devzendo.beanminder.plugin.facade.providemenu;

import org.devzendo.commoncode.patterns.observer.Observer;
import org.devzendo.minimiser.gui.menu.ApplicationMenu;
import org.devzendo.minimiser.gui.menu.MenuFacade;
import org.devzendo.minimiser.gui.tab.TabIdentifier;
import org.devzendo.minimiser.openlist.DatabaseDescriptor;
import org.devzendo.minimiser.openlist.DatabaseEvent;
import org.devzendo.minimiser.openlist.DatabaseOpenedEvent;
import org.devzendo.minimiser.openlist.OpenDatabaseList;
import org.devzendo.minimiser.openlist.DatabaseDescriptor.AttributeIdentifier;
import org.devzendo.minimiser.plugin.facade.providemenu.MenuProvidingFacade;

public class SimpleAccountsMenuProvidingFacade implements MenuProvidingFacade {
    public void initialise(
            final ApplicationMenu globalApplicationMenu,
            final OpenDatabaseList openDatabaseList,
            final MenuFacade menuFacade) {
        // TODO Auto-generated method stub
        openDatabaseList.addDatabaseEventObserver(new Observer<DatabaseEvent>() {
            public void eventOccurred(final DatabaseEvent databaseEvent) {
                if (databaseEvent instanceof DatabaseOpenedEvent) {
                    final DatabaseOpenedEvent databaseOpenedEvent = (DatabaseOpenedEvent) databaseEvent;
                    handleDatabaseOpenedEvent(databaseOpenedEvent);
                }
            }

            private void handleDatabaseOpenedEvent(
                    final DatabaseOpenedEvent databaseOpenedEvent) {
                final DatabaseDescriptor databaseDescriptor = databaseOpenedEvent.getDatabaseDescriptor();
                final ApplicationMenu databaseApplicationMenu = (ApplicationMenu) databaseDescriptor.getAttribute(AttributeIdentifier.ApplicationMenu);
                populateDatabaseApplicationMenu(databaseApplicationMenu);

            }

            private void populateDatabaseApplicationMenu(
                    final ApplicationMenu databaseApplicationMenu) {
                databaseApplicationMenu.addViewMenuTabIdentifier(new TabIdentifier("GOATS", "Goats", false, 'G', "goatTab", null));
                menuFacade.rebuildViewMenu();
            }
        });
    }
}
