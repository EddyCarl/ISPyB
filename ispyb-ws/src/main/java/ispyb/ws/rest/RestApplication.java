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
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Swagger;
import io.swagger.models.auth.BasicAuthDefinition;
import ispyb.ws.rest.mx.AutoprocintegrationRestWebService;
import ispyb.ws.rest.mx.CrystalRestWebService;
import ispyb.ws.rest.mx.EnergyScanRestWebService;
import ispyb.ws.rest.mx.ImageWebService;
import ispyb.ws.rest.mx.PhasingRestWebService;
import ispyb.ws.rest.mx.ProteinRestWebService;
import ispyb.ws.rest.mx.SampleRestWebService;
import ispyb.ws.rest.mx.WorkflowRestWebService;
import ispyb.ws.rest.mx.XFEFluorescenceSpectrumRestWebService;

import ispyb.ws.rest.proposal.DewarRestWebService;
import ispyb.ws.rest.proposal.ProposalRestWebService;
import ispyb.ws.rest.proposal.SessionRestWebService;
import ispyb.ws.rest.proposal.ShippingRestWebService;

import ispyb.ws.rest.saxs.BufferRestWebService;
import ispyb.ws.rest.saxs.DataCollectionRestWebService;
import ispyb.ws.rest.saxs.ExperimentRestWebService;
import ispyb.ws.rest.saxs.FrameRestWebService;
import ispyb.ws.rest.saxs.MacromoleculeRestWebService;
import ispyb.ws.rest.saxs.MeasurementRestWebService;
import ispyb.ws.rest.saxs.ModelingRestWebService;
import ispyb.ws.rest.saxs.SaxsRestWebService;
import ispyb.ws.rest.saxs.SpecimenRestWebService;
import ispyb.ws.rest.saxs.StatsRestWebService;
import ispyb.ws.rest.saxs.StockSolutionRestWebService;
import ispyb.ws.rest.saxs.SubtractionRestWebService;

import ispyb.ws.rest.security.AuthenticationRestWebService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.jaxrs.config.BeanConfig;

import java.util.HashSet;
import java.util.Set;


/**
 * A class extending {@link javax.ws.rs.core.Application} is the portable way to define JAX-RS 2.0 resources,
 * and the {@link javax.ws.rs.ApplicationPath} defines the root path shared by all these resources.
 */
@ApplicationPath("rest")
public class RestApplication extends Application implements ReaderListener
{
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

    beanConfig.setVersion("5.4.3");
    beanConfig.setTitle("ISPyB Web Services - Test title!");
    beanConfig.setDescription( "THIS IS TEXT- LOTS OF TEXT LALALALLALA" );
    beanConfig.setSchemes(new String[]{"http"});
    beanConfig.setHost("192.168.30.200:8080");
    beanConfig.setBasePath("/ispyb/ispyb-ws/rest");
    beanConfig.setResourcePackage("io.swagger.resources");
    beanConfig.setContact( "Carl Edmunds" );
    beanConfig.setScan(true);
  }


  /**
   * Used to retrieve a list of classes that are to be scanned.
   *
   * Swagger requires a list of classes to scan so that it can pick up on any annotations that have been added to them.
   * This method returns a Set that contains all of the classes that contain endpoints that have been created.
   * @return
   */
  @Override
  public Set<Class<?>> getClasses()
  {
    Set<Class<?>> resources = new HashSet<>();

    /** MX **/
    resources.add(AutoprocintegrationRestWebService.class);
    resources.add(CrystalRestWebService.class);
    resources.add(DataCollectionRestWebService.class);
    resources.add(EnergyScanRestWebService.class);
    resources.add(ImageWebService.class);
    resources.add(PhasingRestWebService.class);
    resources.add(ProteinRestWebService.class);
    resources.add(SampleRestWebService.class);
    resources.add(WorkflowRestWebService.class);
    resources.add(XFEFluorescenceSpectrumRestWebService.class);

    /** SAXS **/
    resources.add(BufferRestWebService.class);
    resources.add(DataCollectionRestWebService.class);
    resources.add(ExperimentRestWebService.class);
    resources.add(FrameRestWebService.class);
    resources.add(MacromoleculeRestWebService.class);
    resources.add(MeasurementRestWebService.class);
    resources.add(ModelingRestWebService.class);
    resources.add(SaxsRestWebService.class);
    resources.add(SpecimenRestWebService.class);
    resources.add(StatsRestWebService.class);
    resources.add(StockSolutionRestWebService.class);
    resources.add(SubtractionRestWebService.class);

    /** PROPOSAL **/
    resources.add(DewarRestWebService.class);
    resources.add(ProposalRestWebService.class);
    resources.add(SessionRestWebService.class);
    resources.add(ShippingRestWebService.class);

    /** AUTHENTICATION **/
    resources.add(AuthenticationRestWebService.class);

    resources.add( RestApplication.class );
    resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
    resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

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
    BasicAuthDefinition basicAuthDefinition = new BasicAuthDefinition();
    swagger.addSecurityDefinition( "basicAuth", basicAuthDefinition);
  }
}
