package com.esri.geoevent.devsummit.demo;

import com.esri.ges.core.ConfigurationException;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.*;
import com.esri.ges.core.validation.ValidationException;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.messaging.Messaging;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;
import com.esri.ges.util.Converter;
import com.esri.ges.util.Validator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoProcessor extends GeoEventProcessorBase
{
  Messaging                 messaging;
  GeoEventDefinitionManager geoEventDefinitionManager;

  //private static final Logger LOGGER = LoggerFactory.getLogger(DemoProcessor.class);
  private static final BundleLogger LOGGER       = BundleLoggerFactory.getLogger(DemoProcessor.class);
  private              boolean      isMutated;
  private              String       timeWindow;
  private              boolean      isProcessing = true;
  private              int          counter      = 0;
  private              Integer      incrementer;

  public DemoProcessor(GeoEventProcessorDefinition definition) throws ComponentException
  {
    super(definition);
  }

  @Override
  public synchronized void validate() throws ValidationException
  {
    super.validate();
    if (Integer.parseInt(timeWindow) <= 0)
    {
      throw new ValidationException(LOGGER.translate("VALIDATION_ERROR"));
    }
  }

  private LocalDateTime convertToModernDate(Date date)
  {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  @Override
  public void afterPropertiesSet()
  {
    timeWindow = Validator.compactWhiteSpaces(getProperty(DemoProcessorDefinition.TIME_WINDOW).getValueAsString());
    incrementer = Converter.convertToInteger(timeWindow, 5);
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
    LocalDateTime processingEndTime = processingStartTime.withSecond(incrementer);

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
  public void shutdown()
  {
    super.shutdown();
    //Any clean-up code when the server is shuts down.
  }

  @Override
  public boolean isGeoEventMutator()
  {
    return this.isMutated;
  }

  @Override
  public void onServiceStart()
  {
    //Implement service initial logic as needed
    LOGGER.info("Starting DevSummit Processor Demo......");
  }

  @Override
  public void onServiceStop()
  {
    //Implement service shutdown logic as needed. Clean-up code
    LOGGER.info("Stopping demo processor.....");
  }

}
