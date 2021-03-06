/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

/*
 * This code is licensed to WhimTrip©. For any question, please contact the author of the file.
 */

package fr.whimtrip.ext.jwhtscrapper.service;

import fr.whimtrip.core.util.intrf.ExceptionLogger;
import fr.whimtrip.ext.jwhtscrapper.annotation.Scrapper;
import fr.whimtrip.ext.jwhtscrapper.exception.ScrapperException;
import fr.whimtrip.ext.jwhtscrapper.intfr.HtmlAutoScrapper;
import fr.whimtrip.ext.jwhtscrapper.intfr.ScrapperHelper;
import fr.whimtrip.ext.jwhtscrapper.service.base.*;
import fr.whimtrip.ext.jwhtscrapper.service.holder.ScrappingContext;
import fr.whimtrip.ext.jwhtscrapper.service.scoped.AutomaticScrapperClientImpl;
import fr.whimtrip.ext.jwhtscrapper.service.scoped.DefaultAutomaticInnerScrapperClient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 * <p>Part of project jwht-scrapper</p>
 * <p>Created on 26/07/18</p>
 *
 * <p>
 *     This manager class is a factory pattern class that will nstanciate
 *     ready to use {@link AutomaticScrapperClientImpl}.
 * </p>
 *
 * <p>
 *     This is meant to be used and shared at the application level altough several
 *     different AutomaticScrapperManager can coexist at the application level, it
 *     is usually a common use case to use only one in the application scope.
 * </p>
 *
 * <p>
 *     This class can only be instanciated using the dedicated
 *     {@link AutomaticScrapperManagerBuilder} builder class
 *     providing a gateway to fine tune your scrapper.
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
public final class AutomaticScrapperManagerImpl implements AutomaticScrapperManager {

    private final HtmlAutoScrapperManager htmlAutoScrapperManager;
    private final ExceptionLogger exceptionLogger;
    private final BoundRequestBuilderProcessor requestProcessor;

    /**
     * <p>
     *     This constructor is supposed to be used only by the corresponding Builder
     *     {@link AutomaticScrapperManagerBuilder}.
     * </p>
     * @param htmlAutoScrapperManager the built in {@link HtmlAutoScrapperManager}
     *                                that will be used during the scrapping process.
     * @param exceptionLogger The exception logger that will be used to log any
     *                        thrown exception during the process.
     * @param requestProcessor The request processor implementation to process and
     *                         edit requests.
     */
    AutomaticScrapperManagerImpl(@NotNull final HtmlAutoScrapperManager htmlAutoScrapperManager, @NotNull final ExceptionLogger exceptionLogger, @NotNull final BoundRequestBuilderProcessor requestProcessor) {

        this.htmlAutoScrapperManager = htmlAutoScrapperManager;
        this.exceptionLogger = exceptionLogger;
        this.requestProcessor = requestProcessor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P, H extends ScrapperHelper<P, ?>> AutomaticScrapperClientImpl createClient(@NotNull final List<P> parentObjs, @NotNull final H helper)
    {
        if(parentObjs == null || parentObjs.isEmpty())
            throw new ScrapperException("Cannot scrap empty or null parent object list.");

        ScrappingContext scrappingContext = prepareScrappers(parentObjs, helper);

        return buildAutomaticScrapperClient(scrappingContext);
    }


    /**
     *
     * @param parentObjs the objects that will be used to create the urls
     *                   to scrap and be reused all along the scrapping
     *                   process through the correct {@link ScrapperHelper}.
     * @param helper the helper class that will guide and direct the whole
     *               scrapping process as well as its configurations. Here,
     *               the helper is also supposed to hold an {@link Scrapper}
     *               annotation to describe the scrapping configurations in
     *               order to instanciate the contex.
     * @param <P> the type of Parent Objects
     * @param <H> the type of Helper Clazz
     * @return a valid scraping context from the given input parameters.
     */
    @SuppressWarnings("unchecked")
    private <P, H extends ScrapperHelper<P, ?>> ScrappingContext<P, ?, H> prepareScrappers(List<P> parentObjs, H helper)
    {
        Class<P> parentClazz;
        if((parentObjs).isEmpty())
            parentClazz = (Class<P>) Object.class;
        else
            parentClazz =(Class<P>) ((parentObjs).get(0).getClass());

        return (ScrappingContext<P, ?, H>)
                new ScrappingContext(
                        parentObjs,
                        parentClazz,
                        helper
                );
    }


    /**
     *
     * @param context the scraing context to build an {@link AutomaticScrapperClientImpl}
     *                with.
     * @return the {@link AutomaticScrapperClientImpl} built.
     */
    @NotNull
    private AutomaticScrapperClientImpl buildAutomaticScrapperClient(ScrappingContext context) {

        HttpManagerClient proxyClient =
                this.htmlAutoScrapperManager
                        .createProxyManagerClient(context.getRequestsScrappingContext());
        HtmlAutoScrapper autoScrapper =
                this.htmlAutoScrapperManager
                        .createHtmlAutoScrapper(proxyClient, context);

        AutomaticInnerScrapperClient scrapper = new DefaultAutomaticInnerScrapperClient(context, autoScrapper, exceptionLogger, requestProcessor);

        return new AutomaticScrapperClientImpl(scrapper, exceptionLogger);
    }


}
