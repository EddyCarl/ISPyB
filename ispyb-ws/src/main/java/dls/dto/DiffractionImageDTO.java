package dls.dto;

public class DiffractionImageDTO
{
  private String imageDirectory;

  private String imagePrefix;

  private String imageSuffix;

  private String fileTemplate;

  private int dataCollectionNumber;

  private int rowNumber;

  public String getImageDirectory()
  {
    return imageDirectory;
  }


  public void setImageDirectory( String imageDirectory )
  {
    this.imageDirectory = imageDirectory;
  }


  public String getImagePrefix()
  {
    return imagePrefix;
  }


  public void setImagePrefix( String imagePrefix )
  {
    this.imagePrefix = imagePrefix;
  }


  public String getImageSuffix()
  {
    return imageSuffix;
  }


  public void setImageSuffix( String imageSuffix )
  {
    this.imageSuffix = imageSuffix;
  }


  public String getFileTemplate()
  {
    return fileTemplate;
  }


  public void setFileTemplate( String fileTemplate )
  {
    this.fileTemplate = fileTemplate;
  }


  public int getDataCollectionNumber()
  {
    return dataCollectionNumber;
  }


  public void setDataCollectionNumber( int dataCollectionNumber )
  {
    this.dataCollectionNumber = dataCollectionNumber;
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
