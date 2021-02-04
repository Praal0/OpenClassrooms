package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();
    private List<Neighbour> neighbourfav = new ArrayList<Neighbour>();


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
    /*public List<Neighbour> getFavoriteNeighbours() {
        List<Neighbour> favoriteList = new ArrayList<>();
        for (Neighbour neighbour : getNeighbours()) {
            if (neighbour.isFavorite()) {
                favoriteList.add(neighbour);
            }
        }
        return favoriteList;
    }*/

    public List<Neighbour> getFavoriteNeighbours() {
        return neighbourfav;
    }

    /**
     * Add a favorite neighbour
     * {@param neighbour}
     */

    @Override
    /*public void addFavoriteNeighbour(Neighbour neighbour) {
        for (Neighbour neighbour1 : getNeighbours()) {
            if (neighbour.equals(neighbour1)) {
                neighbour1.setFavorite(true);
            }
        }
    }*/
    public void addFavoriteNeighbour(Neighbour neighbour){
        neighbourfav.add(neighbour);
        neighbour.setFavorite(true);
    }


    /**
     * Delete a favorite neighbour
     * {@param neighbour}
     */

    @Override
    public void deleteFavoriteNeighbour(Neighbour neighbourdel) {
        /*for (Neighbour neighbour1 : getNeighbours()) {
            if (neighbourfav.equals(neighbour1)) {
                neighbour1.setFavorite(false);
            }
        }*/
        neighbourfav.remove(neighbourdel);
        neighbourdel.setFavorite(false);

    }

}
