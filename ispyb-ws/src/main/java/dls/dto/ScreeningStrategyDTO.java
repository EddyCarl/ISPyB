package dls.dto;

public class ScreeningStrategyDTO
{
  private int screeningOutputId;

  private int screeningStrategyId;

  private double phiStart;

  private double phiEnd;

  private double rotation;

  private double exposureTime;

  private double resolution;

  private double completeness;

  private double multiplicity;

  private Byte anomalous;

  private String program;

  private double rankingResolution;

  private double transmission;

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


  public double getPhiStart()
  {
    return phiStart;
  }


  public void setPhiStart( double phiStart )
  {
    this.phiStart = phiStart;
  }


  public double getPhiEnd()
  {
    return phiEnd;
  }


  public void setPhiEnd( double phiEnd )
  {
    this.phiEnd = phiEnd;
  }


  public double getRotation()
  {
    return rotation;
  }


  public void setRotation( double rotation )
  {
    this.rotation = rotation;
  }


  public double getExposureTime()
  {
    return exposureTime;
  }


  public void setExposureTime( double exposureTime )
  {
    this.exposureTime = exposureTime;
  }


  public double getResolution()
  {
    return resolution;
  }


  public void setResolution( double resolution )
  {
    this.resolution = resolution;
  }


  public double getCompleteness()
  {
    return completeness;
  }


  public void setCompleteness( double completeness )
  {
    this.completeness = completeness;
  }


  public double getMultiplicity()
  {
    return multiplicity;
  }


  public void setMultiplicity( double multiplicity )
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


  public double getRankingResolution()
  {
    return rankingResolution;
  }


  public void setRankingResolution( double rankingResolution )
  {
    this.rankingResolution = rankingResolution;
  }


  public double getTransmission()
  {
    return transmission;
  }


  public void setTransmission( double transmission )
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
