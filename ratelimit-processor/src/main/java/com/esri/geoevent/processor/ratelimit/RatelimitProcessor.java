package com.esri.geoevent.processor.ratelimit;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.core.property.Property;
import com.esri.ges.core.validation.ValidationException;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;

public class RatelimitProcessor extends GeoEventProcessorBase
{
    public static final String RATELIMIT = "ratelimit";
    public static final String RATELIMIT_NULL = "${com.esri.geoevent.processor.ratelimit-processor.RATELIMIT_NULL}";
    public static final String RATELIMIT_NEGATIVE = "${com.esri.geoevent.processor.ratelimit-processor.RATELIMIT_NEGATIVE}";

    private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(RatelimitProcessor.class);

    private long    lastReport     = 0;
    private float     maxMessageRate = 1;
    private long     maxMessageRateMS = 1000;
    protected RatelimitProcessor(GeoEventProcessorDefinition definition) throws ComponentException
    {
        super(definition);
    }

    @Override
    public void afterPropertiesSet()
    {
        maxMessageRate=Float.parseFloat(properties.get(RATELIMIT).getValueAsString());
        maxMessageRateMS=(long) (maxMessageRate * 1000.0);
    }

    @Override
    public void validate() throws ValidationException
    {
        Property rateLimitProperty = properties.get(RATELIMIT);
        if (rateLimitProperty == null) {
            throw new ValidationException(RATELIMIT_NULL);
        }
        Float rateLimitValue = Float.parseFloat(rateLimitProperty.getValueAsString());
        if (rateLimitValue <= 0) {
            throw new ValidationException(RATELIMIT_NEGATIVE);
        }
    }

    @Override
    public GeoEvent process(GeoEvent geoEvent) throws Exception
    {
        GeoEvent result = null;
        long timeElapsed = System.currentTimeMillis() - lastReport;
        if ( timeElapsed > maxMessageRateMS )
        {
            // "Ratelimit Processing ... (Limiting output to no more than 1 event every "{0}" second)."
            LOGGER.debug("PROCESSING_DETAILS_MSG", maxMessageRate);
            lastReport = System.currentTimeMillis();
            result = geoEvent;
        }
        return result;
    }
}



