package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();
    private List<Neighbour> neighboursfav = new ArrayList<Neighbour>();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }


    /**
     * {@inheritDoc}
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }

    @Override
    public List<Neighbour> getFavoriteNeighbours() {
        return neighboursfav;
    }

    /**
     * Add a favorite neighbour
     * {@param neighbour}
     */

    @Override
    public void addFavoriteNeighbour(Neighbour neighbour) {
        neighboursfav.add(neighbour);
    }

    /**
     * Delete a favorite neighbour
     * {@param neighbour}
     */

    @Override
    public void deleteFavoriteNeighbour(Neighbour neighbourdel) {
        neighboursfav.remove(neighbourdel);
    }

}
