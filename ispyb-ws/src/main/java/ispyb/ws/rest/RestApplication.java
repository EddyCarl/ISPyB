/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ispyb.ws.rest;

import io.swagger.jaxrs.Reader;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.ReaderListener;

import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;

import io.swagger.models.Swagger;
import io.swagger.models.auth.In;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;


/**
 * A class extending {@link javax.ws.rs.core.Application} is the portable way to define JAX-RS 2.0 resources,
 * and the {@link javax.ws.rs.ApplicationPath} defines the root path shared by all these resources.
 */
@ApplicationPath("rest")
public class RestApplication extends Application implements ReaderListener
{
  private final static String TITLE = "ISPyB Web Services";
  private final static String DESCRIPTION = "This webservice is used to support the Diamond Light Source " +
                                            "SynchLink mobile application.";
  private final static String[] SCHEMES = new String[] { "http" };
  private final static String HOST = "192.168.30.200:8080";
  private final static String BASE_PATH = "/ispyb/ispyb-ws/rest";
  private final static String RESOURCE_PACKAGE = "io.swagger.resources";
  private final static String CONTACT = "Neil Smith";

  /**
   * Constructor that is used to setup the application.
   *
   * The main purpose of this constructor is to setup & configure the Swagger UI information. The BeanConfig is used
   * in order to set various properties that will be displayed in the output Swagger UI page (and the JSON page that
   * it is generated from).
   */
  public RestApplication()
  {
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setTitle( TITLE );
    beanConfig.setDescription( DESCRIPTION );
    beanConfig.setSchemes( SCHEMES );
    beanConfig.setHost( HOST );
    beanConfig.setBasePath( BASE_PATH );
    beanConfig.setResourcePackage( RESOURCE_PACKAGE );
    beanConfig.setContact( CONTACT );
    beanConfig.setScan( true );
  }


  /**
   * Used to retrieve a list of classes that are to be scanned.
   *
   * Swagger requires a list of classes to scan so that it can pick up on any annotations that have been added to them.
   * This method returns a Set that contains all of the classes that contain endpoints that have been created.
   *
   * @return Set<Class<?>> - The classes containing resources that are to be scanned.
   */
  @Override
  public Set<Class<?>> getClasses()
  {
    Set<Class<?>> resources = new HashSet<>();

    // This class
    resources.add( ispyb.ws.rest.RestApplication.class );

    // MX package classes
    resources.add( ispyb.ws.rest.mx.AutoprocintegrationRestWebService.class );
    resources.add( ispyb.ws.rest.mx.CrystalRestWebService.class );
    resources.add( ispyb.ws.rest.mx.DataCollectionRestWebService.class );
    resources.add( ispyb.ws.rest.mx.EnergyScanRestWebService.class );
    resources.add( ispyb.ws.rest.mx.ImageWebService.class );
    resources.add( ispyb.ws.rest.mx.PhasingRestWebService.class );
    resources.add( ispyb.ws.rest.mx.ProteinRestWebService.class );
    resources.add( ispyb.ws.rest.mx.SampleRestWebService.class );
    resources.add( ispyb.ws.rest.mx.WorkflowRestWebService.class );
    resources.add( ispyb.ws.rest.mx.XFEFluorescenceSpectrumRestWebService.class );

    // Saxs package classes
    resources.add( ispyb.ws.rest.saxs.BufferRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.DataCollectionRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.ExperimentRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.FrameRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.MacromoleculeRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.MeasurementRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.ModelingRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.SaxsRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.SpecimenRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.StatsRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.StockSolutionRestWebService.class );
    resources.add( ispyb.ws.rest.saxs.SubtractionRestWebService.class );

    // Proposal package classes
    resources.add( ispyb.ws.rest.proposal.DewarRestWebService.class );
    resources.add( ispyb.ws.rest.proposal.ProposalRestWebService.class );
    resources.add( ispyb.ws.rest.proposal.SessionRestWebService.class );
    resources.add( ispyb.ws.rest.proposal.ShippingRestWebService.class );

    // Security package classes
    resources.add( ispyb.ws.rest.security.AuthenticationRestWebService.class );

    // Swagger classes
    resources.add( io.swagger.jaxrs.listing.ApiListingResource.class );
    resources.add( io.swagger.jaxrs.listing.SwaggerSerializers.class );

    return resources;
  }


  /**
   * Called before the Swagger definition gets populated from scanned classes.
   * Use this method to pre-process the Swagger definition before it gets populated.
   * This method is unused in this instance but must be implemented due to the usage of the ReaderListener interface.
   *
   * @param reader    - The reader used to read annotations and build the Swagger definition
   * @param swagger   - The initial swagger definition
   */
  @Override
  public void beforeScan( Reader reader, Swagger swagger )
  {
  }


  /**
   * Called after a Swagger definition has been populated from scanned classes.
   * Use this method to post-process Swagger definitions.
   * This method is being used to add some authentication scheme information to the Swagger output.
   *
   * @param reader    - The reader used to read annotations and build the Swagger definition
   * @param swagger   - The configured Swagger definition
   */
  @Override
  public void afterScan( Reader reader, Swagger swagger )
  {
    // Set up the authentication scheme (basic authentication)
    ApiKeyAuthDefinition apiKeyAuthDefinition = new ApiKeyAuthDefinition( "api_token", In.HEADER );
    swagger.addSecurityDefinition( "apiKeyAuth", apiKeyAuthDefinition );

    BasicAuthDefinition basicAuthDefinition = new BasicAuthDefinition();
    swagger.addSecurityDefinition( "basicAuth", basicAuthDefinition);
  }
}
