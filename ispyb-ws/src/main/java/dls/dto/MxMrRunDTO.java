package dls.dto;

public class MxMrRunDTO
{
  private Integer mxMrRunId;

  private Boolean success;

  private String message;

  private String pipeline;

  private Float rValueStart;

  private Float rValueEnd;

  private Float rFreeValueStart;

  private Float rFreeValueEnd;

  private String logFile;

  private String commandLine;

  private int rowNumber;


  public Integer getMxMrRunId()
  {
    return mxMrRunId;
  }


  public void setMxMrRunId( Integer mxMrRunId )
  {
    this.mxMrRunId = mxMrRunId;
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


  public int getRowNumber()
  {
    return rowNumber;
  }


  public void setRowNumber( int rowNumber )
  {
    this.rowNumber = rowNumber;
  }
}
