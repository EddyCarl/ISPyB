/*************************************************************************************************
 * This file is part of ISPyB.
 *
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau,
 *                M. Bodin, A. De Maria Antolinos
 ****************************************************************************************************/

package ispyb.server.mx.vos.autoproc;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;
import org.apache.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;


// MxMrRun value object mapping rows in MXMRRun table
@Entity
@Table( name = "MXMRRun" )
public class MxMrRun3VO extends ISPyBValueObject implements Cloneable
{
  private final static Logger LOG = Logger.getLogger( MxMrRun3VO.class );

  // Generate the serialVersionUID using the 'serialver' tool of java and enter it here
  // this prevents later invalid class version exceptions when the value object evolves
  private static final long serialVersionUID = 1234567901234567890L;

  @Id
  @GeneratedValue
  @Column( name = "mxMRRunId" )
  protected Integer mxMRRunId;

  @ManyToOne
  @JoinColumn( name = "autoProcScalingId" )
  private AutoProcScaling3VO autoProcScalingVO;

  @Column( name = "success" )
  protected Boolean success;

  @Column( name = "message" )
  protected String message;

  @Column( name = "pipeline" )
  protected String pipeline;

  @Column( name = "inputCoordFile" )
  protected String inputCoordFile;

  @Column( name = "outputCoordFile" )
  protected String outputCoordFile;

  @Column( name = "inputMTZFile" )
  protected String inputMTZFile;

  @Column( name = "outputMTZFile" )
  protected String outputMTZFile;

  @Column( name = "runDirectory" )
  protected String runDirectory;

  @Column( name = "logFile" )
  protected String logFile;

  @Column( name = "commandLine" )
  protected String commandLine;

  @Column( name = "rValueStart" )
  protected Float rValueStart;

  @Column( name = "rValueEnd" )
  protected Float rValueEnd;

  @Column( name = "rFreeValueStart" )
  protected Float rFreeValueStart;

  @Column( name = "rFreeValueEnd" )
  protected Float rFreeValueEnd;

  @Column( name = "starttime" )
  protected Date startTime;

  @Column( name = "endtime" )
  protected Date endTime;


  /**
   * Default blank constructor used by Hibernate ORM.
   */
  public MxMrRun3VO()
  {
    super();
  }


  /**
   * Full constructor passing all values in as parameters.
   *
   * @param mxMRRunId
   * @param autoProcScalingVO
   * @param success
   * @param message
   * @param pipeline
   * @param inputCoordFile
   * @param outputCoordFile
   * @param inputMTZFile
   * @param outputMTZFile
   * @param runDirectory
   * @param logFile
   * @param commandLine
   * @param rValueStart
   * @param rValueEnd
   * @param rFreeValueStart
   * @param rFreeValueEnd
   * @param startTime
   * @param endTime
   */
  public MxMrRun3VO( Integer mxMRRunId, AutoProcScaling3VO autoProcScalingVO, Boolean success, String message,
                     String pipeline, String inputCoordFile, String outputCoordFile, String inputMTZFile,
                     String outputMTZFile, String runDirectory, String logFile, String commandLine, Float rValueStart,
                     Float rValueEnd, Float rFreeValueStart, Float rFreeValueEnd, Date startTime, Date endTime )
  {
    super();
    this.mxMRRunId = mxMRRunId;
    this.autoProcScalingVO = autoProcScalingVO;
    this.success = success;
    this.message = message;
    this.pipeline = pipeline;
    this.inputCoordFile = inputCoordFile;
    this.outputCoordFile = outputCoordFile;
    this.inputMTZFile = inputMTZFile;
    this.outputMTZFile = outputMTZFile;
    this.runDirectory = runDirectory;
    this.logFile = logFile;
    this.commandLine = commandLine;
    this.rValueStart = rValueStart;
    this.rValueEnd = rValueEnd;
    this.rFreeValueStart = rFreeValueStart;
    this.rFreeValueEnd = rFreeValueEnd;
    this.startTime = startTime;
    this.endTime = endTime;
  }


  /**
   * Constructor used to create an MxMrRun3VO instance using the values from another MxMrRun3VO instance.
   *
   * @param vo - An MxMrRun3VO instance passed in to retrieve values from
   */
  public MxMrRun3VO( MxMrRun3VO vo )
  {
    super();
    this.mxMRRunId = vo.getMxMRRunId();
    this.autoProcScalingVO = vo.getAutoProcScalingVO();
    this.success = vo.getSuccess();
    this.message = vo.getMessage();
    this.pipeline = vo.getPipeline();
    this.inputCoordFile = vo.getInputCoordFile();
    this.outputCoordFile = vo.getOutputCoordFile();
    this.inputMTZFile = vo.getInputMTZFile();
    this.outputMTZFile = vo.getOutputMTZFile();
    this.runDirectory = vo.getRunDirectory();
    this.logFile = vo.getLogFile();
    this.commandLine = vo.getCommandLine();
    this.rValueStart = vo.getrValueStart();
    this.rValueEnd = vo.getrValueEnd();
    this.rFreeValueStart = vo.getrFreeValueStart();
    this.rFreeValueEnd = vo.getrFreeValueEnd();
    this.startTime = vo.getStartTime();
    this.endTime = vo.getEndTime();
  }


  /**
   * Constructor used to create an MxMrRun3VO instance using the values from another MxMrRun3VO instance.
   *
   * @param vo - An MxMrRun3VO instance passed in to retrieve values from
   */
  public void fillVOFromWS( MxMrRun3VO vo )
  {
    this.autoProcScalingVO = null;
    this.success = vo.getSuccess();
    this.message = vo.getMessage();
    this.pipeline = vo.getPipeline();
    this.inputCoordFile = vo.getInputCoordFile();
    this.outputCoordFile = vo.getOutputCoordFile();
    this.inputMTZFile = vo.getInputMTZFile();
    this.outputMTZFile = vo.getOutputMTZFile();
    this.runDirectory = vo.getRunDirectory();
    this.logFile = vo.getLogFile();
    this.commandLine = vo.getCommandLine();
    this.rValueStart = vo.getrValueStart();
    this.rValueEnd = vo.getrValueEnd();
    this.rFreeValueStart = vo.getrFreeValueStart();
    this.rFreeValueEnd = vo.getrFreeValueEnd();
    this.startTime = vo.getStartTime();
    this.endTime = vo.getEndTime();
  }


  // Public getter & setter methods for all fields
  public Integer getMxMRRunId()
  {
    return mxMRRunId;
  }
  public void setMxMRRunId( Integer mxMRRunId )
  {
    this.mxMRRunId = mxMRRunId;
  }


  public AutoProcScaling3VO getAutoProcScalingVO()
  {
    return autoProcScalingVO;
  }
  public void setAutoProcScalingVO( AutoProcScaling3VO autoProcScalingVO )
  {
    this.autoProcScalingVO = autoProcScalingVO;
  }


  public Integer getAutoProcScalingVOId()
  {
    return ( autoProcScalingVO == null ) ? null : autoProcScalingVO.getAutoProcScalingId();
  }


  public Boolean getSuccess()
  {
    return success;
  }
  public void setSuccess( Boolean success )
  {
    this.success = success;
  }


  public String getMessage()
  {
    return message;
  }
  public void setMessage( String message )
  {
    this.message = message;
  }


  public String getPipeline()
  {
    return pipeline;
  }
  public void setPipeline( String pipeline )
  {
    this.pipeline = pipeline;
  }


  public String getInputCoordFile()
  {
    return inputCoordFile;
  }
  public void setInputCoordFile( String inputCoordFile )
  {
    this.inputCoordFile = inputCoordFile;
  }


  public String getOutputCoordFile()
  {
    return outputCoordFile;
  }
  public void setOutputCoordFile( String outputCoordFile )
  {
    this.outputCoordFile = outputCoordFile;
  }


  public String getInputMTZFile()
  {
    return inputMTZFile;
  }
  public void setInputMTZFile( String inputMTZFile )
  {
    this.inputMTZFile = inputMTZFile;
  }


  public String getOutputMTZFile()
  {
    return outputMTZFile;
  }
  public void setOutputMTZFile( String outputMTZFile )
  {
    this.outputMTZFile = outputMTZFile;
  }


  public String getRunDirectory()
  {
    return runDirectory;
  }
  public void setRunDirectory( String runDirectory )
  {
    this.runDirectory = runDirectory;
  }


  public String getLogFile()
  {
    return logFile;
  }
  public void setLogFile( String logFile )
  {
    this.logFile = logFile;
  }


  public String getCommandLine()
  {
    return commandLine;
  }
  public void setCommandLine( String commandLine )
  {
    this.commandLine = commandLine;
  }


  public Float getrValueStart()
  {
    return rValueStart;
  }
  public void setrValueStart( Float rValueStart )
  {
    this.rValueStart = rValueStart;
  }


  public Float getrValueEnd()
  {
    return rValueEnd;
  }
  public void setrValueEnd( Float rValueEnd )
  {
    this.rValueEnd = rValueEnd;
  }


  public Float getrFreeValueStart()
  {
    return rFreeValueStart;
  }
  public void setrFreeValueStart( Float rFreeValueStart )
  {
    this.rFreeValueStart = rFreeValueStart;
  }


  public Float getrFreeValueEnd()
  {
    return rFreeValueEnd;
  }
  public void setrFreeValueEnd( Float rFreeValueEnd )
  {
    this.rFreeValueEnd = rFreeValueEnd;
  }


  public Date getStartTime()
  {
    return startTime;
  }
  public void setStartTime( Date startTime )
  {
    this.startTime = startTime;
  }


  public Date getEndTime()
  {
    return endTime;
  }
  public void setEndTime( Date endTime )
  {
    this.endTime = endTime;
  }


  /**
   * Checks the values of this value object for correctness and completeness. This should
   * be done before persisting the data in the DB.
   *
   * @param create - Should be true if the value object is just being created in the DB,
   *                 this avoids some checks like testing the primary key
   *
   * @throws IllegalArgumentException - If any of the data within the value object is not correct
   */
  @Override
  public void checkValues( boolean create )
  {
    final int maxMessageLength = 255;
    final int maxPipelineLength = 50;
    final int maxInputCoordFileLength = 255;
    final int maxOutputCoordFileLength = 255;
    final int maxInputMZFileLength = 255;
    final int maxOuputMZFileLength = 255;
    final int maxRunDirectoryLength = 255;
    final int maxLogFileLength = 255;
    final int maxCommandLineLength = 255;

    // Ensure autoProcScalingVO instance is non null
    if( this.autoProcScalingVO == null )
    {
      throw new IllegalArgumentException(
                StringUtils.getMessageRequiredField( this.getClass().getSimpleName(), "autoProcScalingVO" ) );
    }

    // Ensure the success value is a valid boolean value
    if( !StringUtils.isBoolean( "" + this.success, true ) )
    {
      throw new IllegalArgumentException(
                StringUtils.getMessageBooleanField( this.getClass().getSimpleName(), "success" ) );
    }

    // Ensure all of the string lengths are valid
    if( !StringUtils.isStringLengthValid( this.message, maxMessageLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "message", maxMessageLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.pipeline, maxPipelineLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "pipeline", maxPipelineLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.inputCoordFile, maxInputCoordFileLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "inputCoordFile", maxInputCoordFileLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.outputCoordFile, maxOutputCoordFileLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "outputCoordFile", maxOutputCoordFileLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.inputMTZFile, maxInputMZFileLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "inputMTZFile", maxInputMZFileLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.outputMTZFile, maxOuputMZFileLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "outputMTZFile", maxOuputMZFileLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.runDirectory, maxRunDirectoryLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "runDirectory", maxRunDirectoryLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.logFile, maxLogFileLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "logFile", maxLogFileLength ) );
    }
    if( !StringUtils.isStringLengthValid( this.commandLine, maxCommandLineLength ) )
    {
      throw new IllegalArgumentException( getMaxLengthErrorMessage( "commandLine", maxCommandLineLength ) );
    }
  }


  /**
   * Utility method that returns an error message when a string value exceeds it's max length. The
   * input field/column name is input along with the max length permitted in order to create the error message.
   *
   * @param field - The input field/column name of the string value
   * @param length - The maximum permitted length of the string value
   *
   * @return - A string error message is returned containing the field name and it's maximum permitted length
   */
  private String getMaxLengthErrorMessage( final String field, final int length )
  {
    return StringUtils.getMessageErrorMaxLength( this.getClass().getSimpleName(), field, length );
  }


  /**
   * Method used to clone the object.
   *
   * @return - Returns a cloned instance of the object
   *
   * @throws CloneNotSupportedException
   */
  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }


  /**
   * Builds a string representation of all of the member fields stored within the class which can be output over a
   * webservice if required.
   *
   * @return - String representation of the member variables stored within the class
   */
  public String toWSString()
  {
    StringBuilder sb = new StringBuilder();

    sb.append( "mxMRRunId = " );
    sb.append( this.mxMRRunId );

    if( this.autoProcScalingVO != null )
    {
      sb.append( ", autoProcScalingId = " );
      sb.append( this.getAutoProcScalingVOId() );
    }

    sb.append( ", success = " );
    sb.append( this.success );

    sb.append( ", message = " );
    sb.append( this.message );

    sb.append( ", pipeline = " );
    sb.append( this.pipeline );

    sb.append( ", inputCoordFile = " );
    sb.append( this.inputCoordFile );

    sb.append( ", outputCoordFile = " );
    sb.append( this.outputCoordFile );

    sb.append( ", inputMTZFile = " );
    sb.append( this.inputMTZFile );

    sb.append( ", outputMTZFile = " );
    sb.append( this.outputMTZFile );

    sb.append( ", runDirectory = " );
    sb.append( this.runDirectory );

    sb.append( ", logFile = " );
    sb.append( this.logFile );

    sb.append( ", commandLine = " );
    sb.append( this.commandLine );

    sb.append( ", rValueStart = " );
    sb.append( this.rValueStart );

    sb.append( ", rValueEnd = " );
    sb.append( this.rValueEnd );

    sb.append( ", rFreeValueStart = " );
    sb.append( this.rFreeValueStart );

    sb.append( ", rFreeValueEnd = " );
    sb.append( this.rFreeValueEnd );

    sb.append( ", startTime = " );
    sb.append( this.startTime );

    sb.append( ", endTime = " );
    sb.append( this.endTime );

    return sb.toString();
  }
}
