package org.ametiste.redgreen.application.request;

import org.ametiste.redgreen.component.ComponentFactory;

/**
 *
 * @since
 */
public interface RequestDriverFactory extends ComponentFactory<RequestDriver> {

    @Override
    RequestDriver createComponent();

}
