package com.esri.geoevent.devsummit.demo;

import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.core.property.PropertyType;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.processor.GeoEventProcessorDefinitionBase;

public class DemoProcessorDefinition extends GeoEventProcessorDefinitionBase
{
  private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(DemoProcessorDefinition.class);

  private String time_window_lbl  = "${com.esri.geoevent.devsummit.demo.devsummit-demo.TIME_WINDOW_LBL}";
  private String time_window_desc = "${com.esri.geoevent.devsummit.demo.devsummit-demo.TIME_WINDOW_DESC}";

  protected static final String TIME_WINDOW = "timeWindow";

  public DemoProcessorDefinition() throws PropertyException
  {
    propertyDefinitions.put(TIME_WINDOW, new PropertyDefinition(TIME_WINDOW, PropertyType.String, 5, time_window_lbl, time_window_desc, null, true, false));
  }

  @Override
  public String getName()
  {
    return "DevSummitProcessorDemo";
  }

  @Override
  public String getLabel()
  {
    return "${com.esri.geoevent.devsummit.demo.devsummit-demo.PROCESSOR_LABEL}";
  }

  @Override
  public String getDescription()
  {
    return "${com.esri.geoevent.devsummit.demo.devsummit-demo.PROCESSOR_DESCRIPTION}";
  }

  @Override
  public String getVersion()
  {
    return "10.6.1";
  }

  @Override
  public String getContactInfo()
  {
    return "youremailaddress@domain.com";
  }

  @Override
  public String getDomain()
  {
    return "com.esri.geoevent.devsummit.demo";
  }
}
