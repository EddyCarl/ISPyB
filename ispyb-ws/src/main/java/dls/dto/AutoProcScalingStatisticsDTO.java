package dls.dto;

public class AutoProcScalingStatisticsDTO
{
  private int autoProcScalingId;

  private int autoProcScalingStatisticsId;

  private String scalingStatisticsType;

  private float resolutionLimitLow;

  private float resolutionLimitHigh;

  private float rMerge;

  private float meanIoverSigI;

  private float completeness;

  private int nTotalUniqueObservations;

  private float multiplicity;

  private float anomalousCompleteness;

  private float anomalousMultiplicity;

  private float ccHalf;

  private float ccAno;

  private int rowNumber;


  public int getAutoProcScalingId()
  {
    return autoProcScalingId;
  }


  public void setAutoProcScalingId( int autoProcScalingId )
  {
    this.autoProcScalingId = autoProcScalingId;
  }


  public int getAutoProcScalingStatisticsId()
  {
    return autoProcScalingStatisticsId;
  }


  public void setAutoProcScalingStatisticsId( int autoProcScalingStatisticsId )
  {
    this.autoProcScalingStatisticsId = autoProcScalingStatisticsId;
  }


  public String getScalingStatisticsType()
  {
    return scalingStatisticsType;
  }


  public void setScalingStatisticsType( String scalingStatisticsType )
  {
    this.scalingStatisticsType = scalingStatisticsType;
  }


  public float getResolutionLimitLow()
  {
    return resolutionLimitLow;
  }


  public void setResolutionLimitLow( float resolutionLimitLow )
  {
    this.resolutionLimitLow = resolutionLimitLow;
  }


  public float getResolutionLimitHigh()
  {
    return resolutionLimitHigh;
  }


  public void setResolutionLimitHigh( float resolutionLimitHigh )
  {
    this.resolutionLimitHigh = resolutionLimitHigh;
  }


  public float getrMerge()
  {
    return rMerge;
  }


  public void setrMerge( float rMerge )
  {
    this.rMerge = rMerge;
  }


  public float getMeanIoverSigI()
  {
    return meanIoverSigI;
  }


  public void setMeanIoverSigI( float meanIoverSigI )
  {
    this.meanIoverSigI = meanIoverSigI;
  }


  public float getCompleteness()
  {
    return completeness;
  }


  public void setCompleteness( float completeness )
  {
    this.completeness = completeness;
  }


  public int getnTotalUniqueObservations()
  {
    return nTotalUniqueObservations;
  }


  public void setnTotalUniqueObservations( int nTotalUniqueObservations )
  {
    this.nTotalUniqueObservations = nTotalUniqueObservations;
  }


  public float getMultiplicity()
  {
    return multiplicity;
  }


  public void setMultiplicity( float multiplicity )
  {
    this.multiplicity = multiplicity;
  }


  public float getAnomalousCompleteness()
  {
    return anomalousCompleteness;
  }


  public void setAnomalousCompleteness( float anomalousCompleteness )
  {
    this.anomalousCompleteness = anomalousCompleteness;
  }


  public float getAnomalousMultiplicity()
  {
    return anomalousMultiplicity;
  }


  public void setAnomalousMultiplicity( float anomalousMultiplicity )
  {
    this.anomalousMultiplicity = anomalousMultiplicity;
  }


  public float getCcHalf()
  {
    return ccHalf;
  }


  public void setCcHalf( float ccHalf )
  {
    this.ccHalf = ccHalf;
  }


  public float getCcAno()
  {
    return ccAno;
  }


  public void setCcAno( float ccAno )
  {
    this.ccAno = ccAno;
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
