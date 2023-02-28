package com.esri.geoevent.processor.ratelimit;

import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.core.property.PropertyType;
import com.esri.ges.processor.GeoEventProcessorDefinitionBase;

public class RatelimitProcessorDefinition extends GeoEventProcessorDefinitionBase
{
    public static final String RATELIMIT       = "ratelimit";
    public static final String RATELIMIT_LABEL = "${com.esri.geoevent.processor.ratelimit-processor.RATELIMIT_LABEL}";
    public static final String RATELIMIT_DESC  = "${com.esri.geoevent.processor.ratelimit-processor.RATELIMIT_DESC}";

    public RatelimitProcessorDefinition()
    {
        try
        {
            PropertyDefinition rateLimitPropertyDefinition = new PropertyDefinition(RATELIMIT, PropertyType.Float, 1.0, RATELIMIT_LABEL, RATELIMIT_DESC, true, false);
            propertyDefinitions.put(RATELIMIT, rateLimitPropertyDefinition);
        }
        catch (PropertyException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getLabel()
    {
        /**
         * Note: by using the ${myBundle-symbolic-name.myProperty} notation, the
         * framework will attempt to replace the string with a localized string in
         * your properties file.
         */
        return "${com.esri.geoevent.processor.ratelimit-processor.PROCESSOR_LABEL}";

    }

    @Override
    public String getDescription()
    {
        /**
         * Note: by using the ${myBundle-symbolic-name.myProperty} notation, the
         * framework will attempt to replace the string with a localized string in
         * your properties file.
         */
        return "${com.esri.geoevent.processor.ratelimit-processor.PROCESSOR_DESC}";
    }
}
