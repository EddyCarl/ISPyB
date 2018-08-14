package dls.dto;

public class ProposalResponseDTO
{
  private int proposalId;

  private String proposalCode;

  private String title;

  private String proposalNumber;

  private String proposalType;

  private int rowNumber;


  public int getProposalId()
  {
    return proposalId;
  }


  public void setProposalId( int proposalId )
  {
    this.proposalId = proposalId;
  }


  public String getProposalCode()
  {
    return proposalCode;
  }


  public void setProposalCode( String proposalCode )
  {
    this.proposalCode = proposalCode;
  }


  public String getTitle()
  {
    return title;
  }


  public void setTitle( String title )
  {
    this.title = title;
  }


  public String getProposalNumber()
  {
    return proposalNumber;
  }


  public void setProposalNumber( String proposalNumber )
  {
    this.proposalNumber = proposalNumber;
  }


  public String getProposalType()
  {
    return proposalType;
  }


  public void setProposalType( String proposalType )
  {
    this.proposalType = proposalType;
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
