package sepm.ss15.e0929003.dao;

import sepm.ss15.e0929003.entities.Jockey;

import java.util.List;

public interface JockeyDAO {

    /**
     * Creates a new jockey.
     * @param jockey the jockey to be created.
     * @return the created jockey(containing the id) if no errors occurred.
     * @throws DAOException if the jockey couldn't be created. Reasons for that:
     *  - jockey is null.
     *  - at least one of the attributes firstName, lastName, country, skill, isDeleted is null.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public Jockey create(Jockey jockey) throws DAOException;

    /**
     * Returns a list of jockey objects, for which the value of the attribute skill is between those ones in the
     * objects 'from' and 'to'. If this attribute is null in both objects(in from and in to) it will be simply ignored for filtering.
     * @param from contains the minimum value for the skill.
     * @param to contains the maximum values for the skill.
     * @return the list of jockeys if no errors occurred.
     * @throws DAOException if the search couldn't be performed. Reasons for that:
     *  - to or from are null.
     *  - the values of the attribute skill are not both set or they are not both null.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public List<Jockey> search(Jockey from, Jockey to) throws DAOException;

    /**
     * Updates a jockey with new values.
     * @param jockey the jockey's new values.
     * @throws DAOException if the jockey couldn't be updated. Reasons for that:
     *  - jockey is null.
     *  - at least one of the attributes id, firstName, lastName, country, skill, isDeleted is null.
     *  - a jockey with that id doesn't exist.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public void update(Jockey jockey) throws DAOException;

    /**
     * Deletes a jockey.
     * @param jockey the jockey to delete.
     * @throws DAOException if the jockey couldn't be deleted. To this, the same reasons as in the update method apply.
     */
    public void delete(Jockey jockey) throws DAOException;
}
