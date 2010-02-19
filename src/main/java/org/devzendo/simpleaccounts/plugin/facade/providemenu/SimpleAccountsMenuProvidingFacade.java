package org.devzendo.simpleaccounts.plugin.facade.providemenu;

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
