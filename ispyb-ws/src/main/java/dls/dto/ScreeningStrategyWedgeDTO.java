package dls.dto;

public class ScreeningStrategyWedgeDTO
{
  private int screeningStrategyWedgeId;

  private int screeningStrategyId;

  private Double resolution;

  private Double completeness;

  private Double multiplicity;

  private int wedgeNumber;

  private Double doseTotal;

  private int numberOfImages;

  private int rowNumber;


  public int getScreeningStrategyWedgeId()
  {
    return screeningStrategyWedgeId;
  }


  public void setScreeningStrategyWedgeId( int screeningStrategyWedgeId )
  {
    this.screeningStrategyWedgeId = screeningStrategyWedgeId;
  }


  public int getScreeningStrategyId()
  {
    return screeningStrategyId;
  }


  public void setScreeningStrategyId( int screeningStrategyId )
  {
    this.screeningStrategyId = screeningStrategyId;
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


  public int getWedgeNumber()
  {
    return wedgeNumber;
  }


  public void setWedgeNumber( int wedgeNumber )
  {
    this.wedgeNumber = wedgeNumber;
  }


  public Double getDoseTotal()
  {
    return doseTotal;
  }


  public void setDoseTotal( Double doseTotal )
  {
    this.doseTotal = doseTotal;
  }


  public int getNumberOfImages()
  {
    return numberOfImages;
  }


  public void setNumberOfImages( int numberOfImages )
  {
    this.numberOfImages = numberOfImages;
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
