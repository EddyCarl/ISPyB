package dls.dto;

public class ScreeningCommentsDTO
{
  private int screeningId;

  private int dataCollectionId;

  private String comments;

  private String shortComments;

  private int rowNumber;


  public int getScreeningId()
  {
    return screeningId;
  }


  public void setScreeningId( int screeningId )
  {
    this.screeningId = screeningId;
  }


  public int getDataCollectionId()
  {
    return dataCollectionId;
  }


  public void setDataCollectionId( int dataCollectionId )
  {
    this.dataCollectionId = dataCollectionId;
  }


  public String getComments()
  {
    return comments;
  }


  public void setComments( String comments )
  {
    this.comments = comments;
  }


  public String getShortComments()
  {
    return shortComments;
  }


  public void setShortComments( String shortComments )
  {
    this.shortComments = shortComments;
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
