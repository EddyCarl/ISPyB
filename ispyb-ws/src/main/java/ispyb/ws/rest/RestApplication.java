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

import io.swagger.annotations.Contact;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.config.Scanner;
import io.swagger.jaxrs.config.SwaggerContextService;
//import io.swagger.models.Info;
//import io.swagger.models.Scheme;
//import io.swagger.models.SecurityRequirement;
//import io.swagger.models.Swagger;
//import io.swagger.models.Tag;
//import io.swagger.models.auth.ApiKeyAuthDefinition;
//import io.swagger.models.auth.In;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import io.swagger.jaxrs.config.BeanConfig;

import io.swagger.annotations.Tag;
import io.swagger.annotations.Info;


/**
 * A class extending {@link javax.ws.rs.core.Application} is the portable way to define JAX-RS 2.0 resources, and the {@link javax.ws.rs.ApplicationPath} defines the root path shared by all these resources.
 */
@ApplicationPath("rest")
@SwaggerDefinition(
  info = @Info(
    description = "Another bloody test",
    version = "1.0",
    title = "Another bloody test title!!!!!!!!!",
    contact = @Contact(
      name = "Carl Edmunds",
      email = "this.is.an.email@address.com",
      url = "www.thisisurl.com"
    )),
schemes = {SwaggerDefinition.Scheme.HTTP},
  tags = {
    @Tag( name = "TAG ONE" , description = "THIS IS TAG ONE"),
    @Tag( name = "TAG 2" , description = "THIS IS TAG 2")
  }
)
public class RestApplication extends Application {



    public RestApplication() {

//      Info info = new Info()
//        .title( "Some new test title" )
//        .description( "Adding some bloat text for test purposes" )
//        .version( "5.4.3" );


//      Swagger swagger = new Swagger().info( info );
//
//      swagger.addSecurityDefinition( "api_key",
//        new ApiKeyAuthDefinition( "api_key", In.HEADER ) );
//
//      swagger.addTag( new Tag()
//        .name( "Test-Tag" )
//        .description( "All of the test endpoints" ) );
//
//      swagger.addTag( new Tag()
//        .name( "Another test tag" )
//        .description( "More rubbish text" ) );
//
//      swagger.addScheme( Scheme.HTTP );
////      swagger.setSchemes( Collections.singletonList( Scheme.HTTP ) );
//      swagger.setHost( "192.168.30.200:8080" );
//      swagger.setBasePath( "/ispyb/ispyb-ws/rest" );

      BeanConfig beanConfig = new BeanConfig();
//        beanConfig.setVersion("5.4.3");
//        beanConfig.setTitle("ISPyB Web Services - Test title!");
//        beanConfig.setSchemes(new String[]{"http"});
//        beanConfig.setHost("192.168.30.200:8080");
//        beanConfig.setBasePath("/ispyb/ispyb-ws/rest");
        beanConfig.setResourcePackage("io.swagger.resources");
        beanConfig.setScan(true);


//        beanConfig.setInfo( info );
//        beanConfig.configure( swagger );



    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>> ();
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

        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        return resources;
    }

//
//
////  @Path( "/ispyb-ws" )
//  public void init(ServletConfig config) throws ServletException
//  {
//    //		super.init(config);
//
//
//    ServletContext context = config.getServletContext();
//
//    //    swagger.setResourcePackage("io.swagger.resources");
//    //    swagger.setScan(true);
//
//    Scanner scanner = new Scanner()
//    {
//      @Override
//      public Set<Class<?>> classes()
//      {
//        return getClasses();
//      }
//
//
//      @Override
//      public boolean getPrettyPrint()
//      {
//        return false;
//      }
//
//
//      @Override
//      public void setPrettyPrint( boolean b )
//      {
//
//      }
//    };
//
//    new SwaggerContextService().withServletConfig( config ).updateSwagger( swagger ).setScanner( scanner );
//  }




  }
