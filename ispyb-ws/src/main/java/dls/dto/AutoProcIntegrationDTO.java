package dls.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoProcIntegrationDTO
{
  private int autoProcIntegrationId;

  private int dataCollectionId;

  private int autoProcProgramId;

  private float cellA;

  private float cellB;

  private float cellC;

  private float cellAlpha;

  private float cellBeta;

  private float cellGamma;

  private int rowNumber;


  public int getAutoProcIntegrationId()
  {
    return autoProcIntegrationId;
  }


  public void setAutoProcIntegrationId( int autoProcIntegrationId )
  {
    this.autoProcIntegrationId = autoProcIntegrationId;
  }


  public int getDataCollectionId()
  {
    return dataCollectionId;
  }


  public void setDataCollectionId( int dataCollectionId )
  {
    this.dataCollectionId = dataCollectionId;
  }


  public int getAutoProcProgramId()
  {
    return autoProcProgramId;
  }


  public void setAutoProcProgramId( int autoProcProgramId )
  {
    this.autoProcProgramId = autoProcProgramId;
  }


  public float getCellA()
  {
    return cellA;
  }


  public void setCellA( float cellA )
  {
    this.cellA = cellA;
  }


  public float getCellB()
  {
    return cellB;
  }


  public void setCellB( float cellB )
  {
    this.cellB = cellB;
  }


  public float getCellC()
  {
    return cellC;
  }


  public void setCellC( float cellC )
  {
    this.cellC = cellC;
  }


  public float getCellAlpha()
  {
    return cellAlpha;
  }


  public void setCellAlpha( float cellAlpha )
  {
    this.cellAlpha = cellAlpha;
  }


  public float getCellBeta()
  {
    return cellBeta;
  }


  public void setCellBeta( float cellBeta )
  {
    this.cellBeta = cellBeta;
  }


  public float getCellGamma()
  {
    return cellGamma;
  }


  public void setCellGamma( float cellGamma )
  {
    this.cellGamma = cellGamma;
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
