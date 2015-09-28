package org.ametiste.redgreen;

import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.line.FailoverLine;
import org.ametiste.redgreen.application.request.RequestDriver;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.RedgreenPair;

/**
 *
 * @since
 */
public class CachingExecuitonLine implements FailoverLine {

    @Override
    public void performRequest(RedgreenRequest rgRequest,
                               RedgreenPair resourcesPair,
                               RequestDriver requestDriver, RedgreenResponse rgResponse) {

        // TODO: кажется RequestDriver это прикол одного конкретно взятого сабсета ExecutionLine,
        // TODO: вот в этой реализации мне нужно два драйвера, один для кэша, второй для дата-порта
        // TODO: причем интерфейс у этих драйверов другой
        // TODO: либо RequestDriver должен быть связан с каждым ресурсом из пары

    }

}
