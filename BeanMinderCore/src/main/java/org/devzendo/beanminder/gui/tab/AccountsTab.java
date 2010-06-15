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
package org.devzendo.beanminder.gui.tab;

import java.awt.Component;

import org.devzendo.minimiser.gui.tab.Tab;
import org.devzendo.minimiser.openlist.DatabaseDescriptor;

/**
 * @author matt
 *
 */
public final class AccountsTab implements Tab {
    private final DatabaseDescriptor mDatabaseDescriptor;

    /**
     * @param databaseDescriptor the current database descriptor
     */
    public AccountsTab(final DatabaseDescriptor databaseDescriptor) {
        this.mDatabaseDescriptor = databaseDescriptor;
    }
    /**
     * {@inheritDoc}
     */
    public void destroy() {
    }

    /**
     * {@inheritDoc}
     */
    public void disposeComponent() {
    }

    /**
     * {@inheritDoc}
     */
    public Component getComponent() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void initComponent() {
    }
}
