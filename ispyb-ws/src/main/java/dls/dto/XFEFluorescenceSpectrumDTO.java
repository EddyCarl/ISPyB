package dls.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XFEFluorescenceSpectrumDTO
{
  private int sessionId;

  private Float exposureTime;

  private String startTime;

  private String jpegScanFileFullPath;

  private Float energy;

  private int rowNumber;


  public int getSessionId()
  {
    return sessionId;
  }


  public void setSessionId( int sessionId )
  {
    this.sessionId = sessionId;
  }


  public Float getExposureTime()
  {
    return exposureTime;
  }


  public void setExposureTime( Float exposureTime )
  {
    this.exposureTime = exposureTime;
  }


  public String getStartTime()
  {
    return startTime;
  }


  public void setStartTime( Date startTime )
  {
    if( startTime != null )
    {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      this.startTime = simpleDateFormat.format( startTime );
    }
  }


  public String getJpegScanFileFullPath()
  {
    return jpegScanFileFullPath;
  }


  public void setJpegScanFileFullPath( String jpegScanFileFullPath )
  {
    this.jpegScanFileFullPath = jpegScanFileFullPath;
  }


  public Float getEnergy()
  {
    return energy;
  }


  public void setEnergy( Float energy )
  {
    this.energy = energy;
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
