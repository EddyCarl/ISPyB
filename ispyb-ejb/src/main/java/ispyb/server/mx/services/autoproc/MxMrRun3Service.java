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
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau,
 *                M. Bodin, A. De Maria Antolinos
 *
 ****************************************************************************************************/

package ispyb.server.mx.services.autoproc;

import ispyb.server.mx.vos.autoproc.MxMrRun3VO;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface MxMrRun3Service
{
  /**
   * Creates a new MxMrRun3VO instance and persists it in the database.
   *
   * @param vo - The MxMrRun3VO entity to persist in the database
   *
   * @return - The MxMrRun3VO entity persisted in the database
   *
   * @throws Exception
   */
  public MxMrRun3VO create( final MxMrRun3VO vo ) throws Exception;


  /**
   * Update the MxMrRun3VO entity persisted in the database with the incoming MxMrRun3VO entities values.
   *
   * @param vo - The input MxMrRun3VO entity containing the updated values to persist in the database
   *
   * @return - The updated MxMrRun3VO entity
   *
   * @throws Exception
   */
  public MxMrRun3VO update( final MxMrRun3VO vo ) throws Exception;


  /**
   * Remove the MxMrRun3VO entity from the database.
   *
   * @param vo - The MxMrRun3VO entity to delete from the database
   *
   * @throws Exception
   */
  public void delete( final MxMrRun3VO vo ) throws Exception;


  /**
   * Remove the MxMrRun3VO entity from the database using its primary key id.
   *
   * @param pk - The primary key id of the MxMrRun3VO entity to delete from the database
   *
   * @throws Exception
   */
  public void deleteByPk( final Integer pk ) throws Exception;


  /**
   * Attempts to find a MxMrRun3VO entity in the database using the provided primary key.
   *
   * @param pk - The primary key of the MxMrRun3VO entity to find in the database (if it exists)
   *
   * @return - Returns the MxMrRun3VO entity found using the primary key if available
   *
   * @throws Exception
   */
  public MxMrRun3VO findByPk( final Integer pk ) throws Exception;


  /**
   * Find all MxMrRun3VO entities stored within the database.
   *
   * @return - A List containing all of the MxMrRun3VO entities stored in the database
   *
   * @throws Exception
   */
  public List<MxMrRun3VO> findAll() throws Exception;


  /**
   *
   * @param autoProcScalingId
   * @return
   * @throws Exception
   */
  public List<MxMrRun3VO> findByAutoProcScalingId( final Integer autoProcScalingId ) throws Exception;
}
