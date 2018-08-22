/*************************************************************************************************
 * This file is part of ISPyB.
 *
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier,
 *                P. Brenchereau, M. Bodin, A. De Maria Antolinos
 *
 ****************************************************************************************************/
package ispyb.server.mx.services.autoproc;

import ispyb.server.mx.vos.autoproc.MxMrRun3VO;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Stateless
public class MxMrRun3ServiceBean implements MxMrRun3Service, MxMrRun3ServiceLocal
{
  private final static Logger LOG = Logger.getLogger( MxMrRun3ServiceBean.class);

  // Used to find a particular MxMrRun3VO entity in the database
  private static String FIND_BY_PK() { return "from MxMrRun3VO vo where vo.mxMRRunId = :pk"; }

  // Used to find all MxMrRun3VO entities in the database
  private static String FIND_ALL() { return "from MxMrRun3VO vo"; }

  @PersistenceContext(unitName = "ispyb_db")
  private EntityManager entityManager;

  public MxMrRun3ServiceBean()
  {
  }

  /**
   * Creates a new MxMrRun3VO instance and persists it in the database.
   *
   * @param vo - The MxMrRun3VO entity to persist in the database
   *
   * @return - The MxMrRun3VO entity persisted in the database
   *
   * @throws Exception - Any exception occurring is thrown
   */
  public MxMrRun3VO create( final MxMrRun3VO vo ) throws Exception
  {
    this.checkAndCompleteData( vo, true );
    this.entityManager.persist( vo );
    return vo;
  }


  /**
   * Update the MxMrRun3VO entity persisted in the database with the incoming MxMrRun3VO entities values.
   *
   * @param vo - The input MxMrRun3VO entity containing the updated values to persist in the database
   *
   * @return - The updated MxMrRun3VO entity
   *
   * @throws Exception - Any exception occurring is thrown
   */
  public MxMrRun3VO update( final MxMrRun3VO vo ) throws Exception
  {
    this.checkAndCompleteData( vo, false );
    return entityManager.merge( vo );
  }


  /**
   * Remove the MxMrRun3VO entity from the database.
   *
   * @param vo - The MxMrRun3VO entity to delete from the database
   *
   * @throws Exception - Any exception occurring is thrown
   */
  public void delete( final MxMrRun3VO vo ) throws Exception
  {
    entityManager.remove(vo);
  }


  /**
   * Remove the MxMrRun3VO entity from the database using its primary key id.
   *
   * @param pk - The primary key id of the MxMrRun3VO entity to delete from the database
   *
   * @throws Exception - Any exception occurring is thrown
   */
  public void deleteByPk( final Integer pk ) throws Exception
  {
    MxMrRun3VO vo = findByPk( pk );
    delete( vo );
  }


  /**
   * Attempts to find a MxMrRun3VO entity in the database using the provided primary key.
   *
   * @param pk - The primary key of the MxMrRun3VO entity to find in the database (if it exists)
   *
   * @return - Returns the MxMrRun3VO entity found using the primary key if available
   *
   * @throws NoResultException - Thrown if no MxMrRun3VO entity with the input primary key is found in the database
   */
  public MxMrRun3VO findByPk( final Integer pk ) throws NoResultException
  {
    try
    {
      return ( MxMrRun3VO )entityManager.createQuery( FIND_BY_PK() ).setParameter( "pk", pk ).getSingleResult();
    }
    catch( NoResultException nre )
    {
      return null;
    }
  }


  /**
   * Find all MxMrRun3VO entities stored within the database.
   *
   * @return - A List containing all of the MxMrRun3VO entities stored in the database
   *
   * @throws Exception - Any exception occurring is thrown
   */
  public List<MxMrRun3VO> findAll() throws Exception
  {
    return entityManager.createQuery( FIND_ALL() ).getResultList();
  }


  /**
   * Checks the input data for integrity and validity.
   *
   * @param vo - The input MxMrRun3VO data that needs to be checked
   * @param create - Whether the object is being created or not
   *
   * @throws IllegalArgumentException - Any incorrect data causes this exception to be thrown with an error message
   */
  private void checkAndCompleteData( MxMrRun3VO vo, boolean create ) throws IllegalArgumentException
  {
    if( create )
    {
      if( vo.getMxMRRunId() != null )
      {
        throw new IllegalArgumentException( "The Primary key is already set! " +
                                            "This must be done automatically. " +
                                            "Please, set it to null!" );
      }
    }
    else
    {
      if( vo.getMxMRRunId() == null )
      {
        throw new IllegalArgumentException( "Primary key is not set for update!" );
      }
    }

    // Check validity of the values in the object
    vo.checkValues( create );
  }
}
