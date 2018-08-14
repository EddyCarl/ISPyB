package dls.dto;

public class BLSampleDTO
{
  private int blSampleId;

  private String name;

  private double loopLength;

  private String loopType;

  private String comments;

  private int crystalId;

  private int rowNumber;


  public int getBlSampleId()
  {
    return blSampleId;
  }


  public void setBlSampleId( int blSampleId )
  {
    this.blSampleId = blSampleId;
  }


  public String getName()
  {
    return name;
  }


  public void setName( String name )
  {
    this.name = name;
  }


  public double getLoopLength()
  {
    return loopLength;
  }


  public void setLoopLength( double loopLength )
  {
    this.loopLength = loopLength;
  }


  public String getLoopType()
  {
    return loopType;
  }


  public void setLoopType( String loopType )
  {
    this.loopType = loopType;
  }


  public String getComments()
  {
    return comments;
  }


  public void setComments( String comments )
  {
    this.comments = comments;
  }


  public int getCrystalId()
  {
    return crystalId;
  }


  public void setCrystalId( int crystalId )
  {
    this.crystalId = crystalId;
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
