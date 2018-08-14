package dls.dto;

public class DataCollectionDTO
{
  private int dataCollectionId;

  private int numberOfImages;

  private int rowNumber;


  public int getDataCollectionId()
  {
    return dataCollectionId;
  }


  public void setDataCollectionId( int dataCollectionId )
  {
    this.dataCollectionId = dataCollectionId;
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
