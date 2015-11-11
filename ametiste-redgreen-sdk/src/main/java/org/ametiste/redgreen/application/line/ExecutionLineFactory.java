package org.ametiste.redgreen.application.line;

import org.ametiste.redgreen.component.ComponentFactory;

/**
 *
 * @since
 */
public interface ExecutionLineFactory extends ComponentFactory<ExecutionLine> {

    @Override
    ExecutionLine createComponent();

}
