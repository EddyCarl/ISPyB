package dls.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailedDataCollectionDTO
{
  private int dataCollectionId;

  private Integer blSampleId;

  private String startTime;

  private int numberOfImages;

  private int sessionId;

  private double exposureTime;

  private String imagePrefix;

  private double wavelength;

  private double resolution;

  private String imageDirectory;

  private String comments;

  private double axisStart;

  private double axisEnd;

  private double axisRange;

  private double omegaStart;

  private double overlap;

  private double beamSizeAtSampleX;

  private double beamSizeAtSampleY;

  private double transmission;

  private String xtalSnapshotFullPath1;

  private String xtalSnapshotFullPath2;

  private String xtalSnapshotFullPath3;

  private String xtalSnapshotFullPath4;

  private int rowNumber;

  public int getDataCollectionId()
  {
    return dataCollectionId;
  }


  public void setDataCollectionId( int dataCollectionId )
  {
    this.dataCollectionId = dataCollectionId;
  }


  public Integer getBlSampleId()
  {
    return blSampleId;
  }


  public void setBlSampleId( Integer blSampleId )
  {
    this.blSampleId = blSampleId;
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


  public int getNumberOfImages()
  {
    return numberOfImages;
  }


  public void setNumberOfImages( int numberOfImages )
  {
    this.numberOfImages = numberOfImages;
  }


  public int getSessionId()
  {
    return sessionId;
  }


  public void setSessionId( int sessionId )
  {
    this.sessionId = sessionId;
  }


  public double getExposureTime()
  {
    return exposureTime;
  }


  public void setExposureTime( double exposureTime )
  {
    this.exposureTime = exposureTime;
  }


  public String getImagePrefix()
  {
    return imagePrefix;
  }


  public void setImagePrefix( String imagePrefix )
  {
    this.imagePrefix = imagePrefix;
  }


  public double getWavelength()
  {
    return wavelength;
  }


  public void setWavelength( double wavelength )
  {
    this.wavelength = wavelength;
  }


  public double getResolution()
  {
    return resolution;
  }


  public void setResolution( double resolution )
  {
    this.resolution = resolution;
  }


  public String getImageDirectory()
  {
    return imageDirectory;
  }


  public void setImageDirectory( String imageDirectory )
  {
    this.imageDirectory = imageDirectory;
  }


  public String getComments()
  {
    return comments;
  }


  public void setComments( String comments )
  {
    this.comments = comments;
  }


  public double getAxisStart()
  {
    return axisStart;
  }


  public void setAxisStart( double axisStart )
  {
    this.axisStart = axisStart;
  }


  public double getAxisEnd()
  {
    return axisEnd;
  }


  public void setAxisEnd( double axisEnd )
  {
    this.axisEnd = axisEnd;
  }


  public double getAxisRange()
  {
    return axisRange;
  }


  public void setAxisRange( double axisRange )
  {
    this.axisRange = axisRange;
  }


  public double getOmegaStart()
  {
    return omegaStart;
  }


  public void setOmegaStart( double omegaStart )
  {
    this.omegaStart = omegaStart;
  }


  public double getOverlap()
  {
    return overlap;
  }


  public void setOverlap( double overlap )
  {
    this.overlap = overlap;
  }


  public double getBeamSizeAtSampleX()
  {
    return beamSizeAtSampleX;
  }


  public void setBeamSizeAtSampleX( double beamSizeAtSampleX )
  {
    this.beamSizeAtSampleX = beamSizeAtSampleX;
  }


  public double getBeamSizeAtSampleY()
  {
    return beamSizeAtSampleY;
  }


  public void setBeamSizeAtSampleY( double beamSizeAtSampleY )
  {
    this.beamSizeAtSampleY = beamSizeAtSampleY;
  }


  public double getTransmission()
  {
    return transmission;
  }


  public void setTransmission( double transmission )
  {
    this.transmission = transmission;
  }


  public String getXtalSnapshotFullPath1()
  {
    return xtalSnapshotFullPath1;
  }


  public void setXtalSnapshotFullPath1( String xtalSnapshotFullPath1 )
  {
    this.xtalSnapshotFullPath1 = xtalSnapshotFullPath1;
  }


  public String getXtalSnapshotFullPath2()
  {
    return xtalSnapshotFullPath2;
  }


  public void setXtalSnapshotFullPath2( String xtalSnapshotFullPath2 )
  {
    this.xtalSnapshotFullPath2 = xtalSnapshotFullPath2;
  }


  public String getXtalSnapshotFullPath3()
  {
    return xtalSnapshotFullPath3;
  }


  public void setXtalSnapshotFullPath3( String xtalSnapshotFullPath3 )
  {
    this.xtalSnapshotFullPath3 = xtalSnapshotFullPath3;
  }


  public String getXtalSnapshotFullPath4()
  {
    return xtalSnapshotFullPath4;
  }


  public void setXtalSnapshotFullPath4( String xtalSnapshotFullPath4 )
  {
    this.xtalSnapshotFullPath4 = xtalSnapshotFullPath4;
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
