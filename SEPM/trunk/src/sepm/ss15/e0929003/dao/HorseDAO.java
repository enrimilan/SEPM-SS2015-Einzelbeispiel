package sepm.ss15.e0929003.dao;

import sepm.ss15.e0929003.entities.Horse;

import java.util.List;

public interface HorseDAO {

    /**
     * Creates a new horse.
     * @param horse the horse to be created.
     * @return the created horse(containing the id) if no errors occurred.
     * @throws DAOException if the horse couldn't be created. Reasons for that:
     *  - horse is null.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public Horse create(Horse horse) throws DAOException;

    /**
     * Returns a list of horse objects, for which the values of the attributes age, minSpeed and maxSpeed are between those ones in the
     * objects 'from' and 'to'.
     * @param from contains the minimum values for age, minSpeed and maxSpeed.
     * @param to contains the maximum values for age, minSpeed and maxSpeed.
     * @return the list of horses if no errors occurred.
     * @throws DAOException if the search couldn't be performed. Reasons for that:
     *  - to or from are null.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public List<Horse> search(Horse from, Horse to) throws DAOException;

    /**
     * Updates a horse with new values.
     * @param horse the horse's new values.
     * @throws DAOException if the horse couldn't be updated. Reasons for that:
     *  - horse is null.
     *  - a horse with that id doesn't exist.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public void update(Horse horse) throws DAOException;

    /**
     * Deletes a horse.
     * @param horse the horse to delete.
     * @throws DAOException if the horse couldn't be deleted. To this, the same reasons as in the update method apply.
     */
    public void delete(Horse horse) throws DAOException;
}
