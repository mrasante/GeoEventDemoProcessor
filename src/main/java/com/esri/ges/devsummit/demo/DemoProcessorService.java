package com.esri.ges.devsummit.demo;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.messaging.Messaging;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;

public class DemoProcessorService extends GeoEventProcessorServiceBase
{
  private Messaging                 messaging;
  private GeoEventDefinitionManager manager;

  public DemoProcessorService() throws PropertyException
  {
    definition = new DemoProcessorDefinition();
  }

  public GeoEventProcessor create() throws ComponentException
  {
    DemoProcessor demoProcessor = new DemoProcessor(definition);
    demoProcessor.setGeoEventDefinitionManager(manager);
    demoProcessor.setMessaging(messaging);
    return demoProcessor;
  }

  public void setManager(GeoEventDefinitionManager definitionManager)
  {
    this.manager = definitionManager;
  }

  public void setMessaging(Messaging messaging)
  {
    this.messaging = messaging;
  }

}
