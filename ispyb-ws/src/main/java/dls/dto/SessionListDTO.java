package dls.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionListDTO
{
  private int sessionId;

  private int proposalId;

  private String startDate;

  private String beamlineName;

  private String beamLineOperator;

  private String projectCode;

  private int visitNumber;

  private int rowNumber;


  public int getSessionId()
  {
    return sessionId;
  }


  public void setSessionId( int sessionId )
  {
    this.sessionId = sessionId;
  }


  public int getProposalId()
  {
    return proposalId;
  }


  public void setProposalId( int proposalId )
  {
    this.proposalId = proposalId;
  }


  public String getStartDate()
  {
    return startDate;
  }


  public void setStartDate( Date startDate )
  {
    if( startDate != null )
    {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      this.startDate = simpleDateFormat.format( startDate );
    }
  }


  public String getBeamlineName()
  {
    return beamlineName;
  }


  public void setBeamlineName( String beamlineName )
  {
    this.beamlineName = beamlineName;
  }


  public String getBeamLineOperator()
  {
    return beamLineOperator;
  }


  public void setBeamLineOperator( String beamLineOperator )
  {
    this.beamLineOperator = beamLineOperator;
  }


  public String getProjectCode()
  {
    return projectCode;
  }


  public void setProjectCode( String projectCode )
  {
    this.projectCode = projectCode;
  }


  public int getVisitNumber()
  {
    return visitNumber;
  }


  public void setVisitNumber( int visitNumber )
  {
    this.visitNumber = visitNumber;
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
