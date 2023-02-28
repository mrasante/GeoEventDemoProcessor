package com.esri.geoevent.processor.ratelimit;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;


public class RatelimitProcessorService extends GeoEventProcessorServiceBase {
    public RatelimitProcessorService ()
    {
        definition = new RatelimitProcessorDefinition();
    }

    @Override
    public GeoEventProcessor create() throws ComponentException
    {
        return new RatelimitProcessor(definition);
    }
}
