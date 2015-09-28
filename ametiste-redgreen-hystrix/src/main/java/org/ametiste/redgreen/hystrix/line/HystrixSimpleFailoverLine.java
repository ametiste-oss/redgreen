package org.ametiste.redgreen.hystrix.line;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.ametiste.redgreen.application.RedgreenRequest;
import org.ametiste.redgreen.application.line.FailoverLine;
import org.ametiste.redgreen.application.process.FailoverProcess;
import org.ametiste.redgreen.application.response.RedgreenResponse;
import org.ametiste.redgreen.bundle.RedgreenPair;
import org.ametiste.redgreen.hystrix.configuration.HystrixSimpleFailoverLineConfiguration;
import org.ametiste.redgreen.application.request.RequestDriver;

/**
 * <p>
 *  This {@code line} implemented using {@code Hystrix} commands framework, each request line is
 *  composed by two parts - <i>command</i> and <i>fallback</i>.
 * </p>
 *
 * <p>
 *  A <i>main</i> resource of a bundle would be used as target to execute <i>command</i>
 *  part of line, and if any error was occured, each <i>red</i> resource would be tried
 *  one by one as <i>failure</i> part of line.
 * </p>
 *
 * <p>
 *  Note, all <i>red</i> resources would be executed in the same {@code Hystrix Fallback Command},
 *  so the one thread/symaphore will be used.
 * </p>
 *
 * <p>
 *  Note, {@code Hystrix} circuit breaker behaviour is controlled global for each {@code line}
 *  instance. See {@link HystrixSimpleFailoverLineConfiguration} for details.
 * </p>
 *
 * @see HystrixSimpleFailoverLineConfiguration
 * @since 0.1.0
 */
// TODO: rewrite javadoc in way of FailoverProcess abstraction usage
// TODO: rename to HystrixFailoverLine or HystrixJavanicaFailoverLine
public class HystrixSimpleFailoverLine implements FailoverLine {

    /**
     * Defines {@code Hystrix} command key, this constant can be used as the reference
     * to this line execution command.
     *
     * @since 0.1.0
     */
    public static final String HYSTRIX_COMMAND_KEY = "SimpleFailoverLineExecution";

    private final FailoverProcess failoverProcess;

    public HystrixSimpleFailoverLine(FailoverProcess failoverProcess) {

        if (failoverProcess == null) {
            throw new IllegalArgumentException("Failover line requires not null failover process to execute.");
        }

        this.failoverProcess = failoverProcess;
    }

    @Override
    @HystrixCommand(commandKey=HYSTRIX_COMMAND_KEY, fallbackMethod = "performFallback")
    public void performRequest(RedgreenRequest rgRequest,
                               RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse rgResponse) {
        failoverProcess
                .performMain(rgRequest, resourcesPair, requestDriver, rgResponse);
    }

    public void performFallback(RedgreenRequest rgRequest,
                                RedgreenPair resourcesPair, RequestDriver requestDriver, RedgreenResponse rgResponse) {
        failoverProcess
                .performFallback(rgRequest, resourcesPair, requestDriver, rgResponse);
    }

}
