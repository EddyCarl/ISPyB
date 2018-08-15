package dls.dto;

public class ScreeningStrategyDTO
{
  private int screeningOutputId;

  private int screeningStrategyId;

  private Double phiStart;

  private Double phiEnd;

  private Double rotation;

  private Double exposureTime;

  private Double resolution;

  private Double completeness;

  private Double multiplicity;

  private Byte anomalous;

  private String program;

  private Double rankingResolution;

  private Double transmission;

  private int rowNumber;


  public int getScreeningOutputId()
  {
    return screeningOutputId;
  }


  public void setScreeningOutputId( int screeningOutputId )
  {
    this.screeningOutputId = screeningOutputId;
  }


  public int getScreeningStrategyId()
  {
    return screeningStrategyId;
  }


  public void setScreeningStrategyId( int screeningStrategyId )
  {
    this.screeningStrategyId = screeningStrategyId;
  }


  public Double getPhiStart()
  {
    return phiStart;
  }


  public void setPhiStart( Double phiStart )
  {
    this.phiStart = phiStart;
  }


  public Double getPhiEnd()
  {
    return phiEnd;
  }


  public void setPhiEnd( Double phiEnd )
  {
    this.phiEnd = phiEnd;
  }


  public Double getRotation()
  {
    return rotation;
  }


  public void setRotation( Double rotation )
  {
    this.rotation = rotation;
  }


  public Double getExposureTime()
  {
    return exposureTime;
  }


  public void setExposureTime( Double exposureTime )
  {
    this.exposureTime = exposureTime;
  }


  public Double getResolution()
  {
    return resolution;
  }


  public void setResolution( Double resolution )
  {
    this.resolution = resolution;
  }


  public Double getCompleteness()
  {
    return completeness;
  }


  public void setCompleteness( Double completeness )
  {
    this.completeness = completeness;
  }


  public Double getMultiplicity()
  {
    return multiplicity;
  }


  public void setMultiplicity( Double multiplicity )
  {
    this.multiplicity = multiplicity;
  }


  public Byte getAnomalous()
  {
    return anomalous;
  }


  public void setAnomalous( Byte anomalous )
  {
    this.anomalous = anomalous;
  }


  public String getProgram()
  {
    return program;
  }


  public void setProgram( String program )
  {
    this.program = program;
  }


  public Double getRankingResolution()
  {
    return rankingResolution;
  }


  public void setRankingResolution( Double rankingResolution )
  {
    this.rankingResolution = rankingResolution;
  }


  public Double getTransmission()
  {
    return transmission;
  }


  public void setTransmission( Double transmission )
  {
    this.transmission = transmission;
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
