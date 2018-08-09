package dls.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EnergyScanDataDTO
{
  private int sessionId;

  private int energyScanId;

  private String element;

  private double startEnergy;

  private double endEnergy;

  private double peakEnergy;

  private double peakFPrime;

  private double peakFDoublePrime;

  private String edgeEnergy;

  private String startTime;

  private double inflectionEnergy;

  private double inflectionFPrime;

  private double inflectionFDoublePrime;

  private String jpegChoochFileFullPath;

  private int rowNumber;


  public int getSessionId()
  {
    return sessionId;
  }


  public void setSessionId( int sessionId )
  {
    this.sessionId = sessionId;
  }


  public int getEnergyScanId()
  {
    return energyScanId;
  }


  public void setEnergyScanId( int energyScanId )
  {
    this.energyScanId = energyScanId;
  }


  public String getElement()
  {
    return element;
  }


  public void setElement( String element )
  {
    this.element = element;
  }


  public double getStartEnergy()
  {
    return startEnergy;
  }


  public void setStartEnergy( double startEnergy )
  {
    this.startEnergy = startEnergy;
  }


  public double getEndEnergy()
  {
    return endEnergy;
  }


  public void setEndEnergy( double endEnergy )
  {
    this.endEnergy = endEnergy;
  }


  public double getPeakEnergy()
  {
    return peakEnergy;
  }


  public void setPeakEnergy( double peakEnergy )
  {
    this.peakEnergy = peakEnergy;
  }


  public double getPeakFPrime()
  {
    return peakFPrime;
  }


  public void setPeakFPrime( double peakFPrime )
  {
    this.peakFPrime = peakFPrime;
  }


  public double getPeakFDoublePrime()
  {
    return peakFDoublePrime;
  }


  public void setPeakFDoublePrime( double peakFDoublePrime )
  {
    this.peakFDoublePrime = peakFDoublePrime;
  }


  public String getEdgeEnergy()
  {
    return edgeEnergy;
  }


  public void setEdgeEnergy( String edgeEnergy )
  {
    this.edgeEnergy = edgeEnergy;
  }


  public String getStartTime()
  {
    return startTime;
  }


  public void setStartTime( Date startTime )
  {
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    this.startTime = sdf.format( startTime );
  }


  public double getInflectionEnergy()
  {
    return inflectionEnergy;
  }


  public void setInflectionEnergy( double inflectionEnergy )
  {
    this.inflectionEnergy = inflectionEnergy;
  }


  public double getInflectionFPrime()
  {
    return inflectionFPrime;
  }


  public void setInflectionFPrime( double inflectionFPrime )
  {
    this.inflectionFPrime = inflectionFPrime;
  }


  public double getInflectionFDoublePrime()
  {
    return inflectionFDoublePrime;
  }


  public void setInflectionFDoublePrime( double inflectionFDoublePrime )
  {
    this.inflectionFDoublePrime = inflectionFDoublePrime;
  }


  public String getJpegChoochFileFullPath()
  {
    return jpegChoochFileFullPath;
  }


  public void setJpegChoochFileFullPath( String jpegChoochFileFullPath )
  {
    this.jpegChoochFileFullPath = jpegChoochFileFullPath;
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
