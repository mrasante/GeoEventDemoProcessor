package com.esri.ges.devsummit.demo;

import com.esri.ges.core.ConfigurationException;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.*;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.messaging.Messaging;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoProcessor extends GeoEventProcessorBase
{
  Messaging                 messaging;
  GeoEventDefinitionManager geoEventDefinitionManager;

  private static final Logger LOGGER = LoggerFactory.getLogger(DemoProcessor.class);

  private boolean isMutated;
  private String  timeWindow;
  private boolean isProcessing = true;
  private int     counter      = 0;

  public DemoProcessor(GeoEventProcessorDefinition definition) throws ComponentException
  {
    super(definition);
  }

  private LocalDateTime convertToModernDate(Date date)
  {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  @Override
  public void afterPropertiesSet()
  {
    timeWindow = getProperty(DemoProcessorDefinition.TIME_WINDOW).getValueAsString();
  }

  @Override
  public GeoEvent process(GeoEvent geoEvent)
  {
    GeoEvent modifiedGeoEvent = null;
    List<LocalDateTime> timerCache = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    timerCache.add(now);
    Date date;
    LocalDateTime receivedTime;
    LocalDateTime processingStartTime = counter == 0 ? now : timerCache.get(0);
    LocalDateTime processingEndTime = processingStartTime.withSecond(Integer.parseInt(timeWindow));

    date = geoEvent.getReceivedTime();
    receivedTime = convertToModernDate(date);
    if (isProcessing)
    {
      counter++;
      if (receivedTime.isBefore(processingEndTime))
      {
        modifiedGeoEvent = modifyGeoEvent(geoEvent);
      }
      else
      {
        isProcessing = false;
        if (counter > 0)
        {
          counter = 0;
          timerCache.clear();
        }
        return null;
      }
    }
    else if (receivedTime.isAfter(processingEndTime))
    {
      isProcessing = true;
    }
    return modifiedGeoEvent;
  }

  private GeoEvent modifyGeoEvent(GeoEvent geoEvent)
  {
    try
    {
      FieldDefinition additionalField = new DefaultFieldDefinition("Arrival_Time", FieldType.Date);
      List<FieldDefinition> fieldDefinitionList = new ArrayList<>();
      fieldDefinitionList.add(additionalField);
      geoEvent.getGeoEventDefinition().augment(fieldDefinitionList);
      geoEvent.setField("Arrival_Time", geoEvent.getReceivedTime());
      isMutated = true;
    }
    catch (ConfigurationException | FieldException error)
    {
      LOGGER.error("GeoEvent Modification Exception: ", error);
      isMutated = false;
      return null;
    }
    return geoEvent;
  }

  public void setMessaging(Messaging serviceMessaging)
  {
    this.messaging = serviceMessaging;
  }

  public void setGeoEventDefinitionManager(GeoEventDefinitionManager serviceDefManager)
  {
    this.geoEventDefinitionManager = serviceDefManager;
  }

  @Override
  public boolean isGeoEventMutator()
  {
    return this.isMutated;
  }

  @Override
  public void onServiceStart()
  {
    super.onServiceStart();
    LOGGER.info("Starting DevSummit Processor Demo......");
  }

  @Override
  public void onServiceStop()
  {
    super.onServiceStop();
    LOGGER.info("Stopping demo processor.....");
  }

}
