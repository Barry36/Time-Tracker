package com.cs446.group18.timetracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuadTreeNode {

    /**
     * Represents the whole rectangle of this node
     * ---------
     * |       |
     * |       |
     * |       |
     * ---------
     */
    public Rectangle2D bounds;

    /**
     * Represents the top left node of this node
     * ---------
     * | x |   |
     * |---|---|
     * |   |   |
     * ---------
     */
    public QuadTreeNode topLeftNode;

    /**
     * Represents the top right node of this node
     * ---------
     * |   | x |
     * |---|---|
     * |   |   |
     * ---------
     */
    public QuadTreeNode topRightNode;

    /**
     * Represents the bottom left node of this node
     * ---------
     * |   |   |
     * |---|---|
     * | x |   |
     * ---------
     */
    public QuadTreeNode bottomLeftNode;

    /**
     * Represents the bottom right node of this node
     * ---------
     * |   |   |
     * |---|---|
     * |   | x |
     * ---------
     */
    public QuadTreeNode bottomRightNode;

    /**
     * List of points of interest A.K.A neighbours inside this node
     * this list is only filled in the deepest nodes
     */
    public List<Neighbour> neighbours = new ArrayList<>();

    /**
     * Creates a new node
     *
     * @param latitude       node's Y start point
     * @param longitude      node's X start point
     * @param latitudeRange  node's height
     * @param longitudeRange node's width
     */
    public QuadTreeNode(double latitude, double longitude, double latitudeRange, double longitudeRange) {
        bounds = new Rectangle2D(longitude, latitude, longitudeRange, latitudeRange);
    }

    /**
     * Adds a neighbour in the quadtree.
     * This method will navigate and create nodes if necessary, until the smallest (deepest) node is reached
     *
     * @param neighbour
     */
    public void addNeighbour(Neighbour neighbour, double deepestNodeSize) {
        double halfSize = bounds.width * .5f;
        if (halfSize < deepestNodeSize) {
            neighbours.add(neighbour);
            return;
        }

        QuadTreeNode node = locateAndCreateNodeForPoint(neighbour.getLatitude(), neighbour.getLongitude());
        node.addNeighbour(neighbour, deepestNodeSize);
    }

    /**
     * Removes a neighbour from the quadtree
     *
     * @param id the neighbour's id
     * @return if the neighbour existed and was removed
     */
    public boolean removeNeighbour(long id) {
        for (Neighbour neighbor : neighbours) {
            if (id == neighbor.getId()) {
                neighbours.remove(neighbor);
                return true;
            }
        }

        if (topLeftNode != null) {
            if (topLeftNode.removeNeighbour(id))
                return true;
        }

        if (bottomLeftNode != null) {
            if (bottomLeftNode.removeNeighbour(id))
                return true;
        }

        if (topRightNode != null) {
            if (topRightNode.removeNeighbour(id))
                return true;
        }

        if (bottomRightNode != null) {
            if (bottomRightNode.removeNeighbour(id))
                return true;
        }

        return false;
    }

    /**
     * Recursively search for neighbours inside the given rectangle
     *
     * @param neighbourSet     a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    public void findNeighboursWithinRectangle(Set<Neighbour> neighbourSet, Rectangle2D rangeAsRectangle) {
        boolean end;

        // In case of containing the whole area of interest
        if (bounds.contains(rangeAsRectangle)) {
            end = true;

            // If end is true, it means that we are on the deepest node
            // otherwise we should keep going deeper

            if (topLeftNode != null) {
                topLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (bottomLeftNode != null) {
                bottomLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (topRightNode != null) {
                topRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (bottomRightNode != null) {
                bottomRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }


            if (end)
                addNeighbors(true, neighbourSet, rangeAsRectangle);

            return;
        }

        // In case of intersection with the area of interest
        if (bounds.intersects(rangeAsRectangle)) {
            end = true;

            // If end is true, it means that we are on the deepest node
            // otherwise we should keep going deeper

            if (topLeftNode != null) {
                topLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (bottomLeftNode != null) {
                bottomLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (topRightNode != null) {
                topRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (bottomRightNode != null) {
                bottomRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (end)
                addNeighbors(false, neighbourSet, rangeAsRectangle);
        }
    }

    /**
     * Adds neighbours to the found set
     *
     * @param contains         if the rangeAsRectangle is contained inside the node
     * @param neighborSet      a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    private void addNeighbors(boolean contains, Set<Neighbour> neighborSet, Rectangle2D rangeAsRectangle) {
        if (contains) {
            neighborSet.addAll(neighbours);
            return;
        }

        findAll(neighborSet, rangeAsRectangle);
    }

    /**
     * If the rangeAsRectangle is not contained inside this node we must
     * search for neighbours that are contained inside the rangeAsRectangle
     *
     * @param neighborSet      a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    private void findAll(Set<Neighbour> neighborSet, Rectangle2D rangeAsRectangle) {
        for (Neighbour neighbor : neighbours) {
            if (rangeAsRectangle.contains(neighbor.getLongitude(), neighbor.getLatitude()))
                neighborSet.add(neighbor);
        }
    }

    /**
     * This methods finds and returns in which of the 4 child nodes the latitude and longitude is located.
     * If the node does not exist, it is created.
     *
     * @param latitude
     * @param longitude
     * @return the node that contains the desired latitude and longitude
     */
    public QuadTreeNode locateAndCreateNodeForPoint(double latitude, double longitude) {
        double halfWidth = bounds.width * .5f;
        double halfHeight = bounds.height * .5f;

        if (longitude < bounds.x + halfWidth) {
            if (latitude < bounds.y + halfHeight)
                return topLeftNode != null ? topLeftNode : (topLeftNode = new QuadTreeNode(bounds.y, bounds.x, halfHeight, halfWidth));

            return bottomLeftNode != null ? bottomLeftNode : (bottomLeftNode = new QuadTreeNode(bounds.y + halfHeight, bounds.x, halfHeight, halfWidth));
        }

        if (latitude < bounds.y + halfHeight)
            return topRightNode != null ? topRightNode : (topRightNode = new QuadTreeNode(bounds.y, bounds.x + halfWidth, halfHeight, halfWidth));

        return bottomRightNode != null ? bottomRightNode : (bottomRightNode = new QuadTreeNode(bounds.y + halfHeight, bounds.x + halfWidth, halfHeight, halfWidth));
    }

    public double getLongitude() {
        return bounds.x;
    }

    public double getLatitude() {
        return bounds.y;
    }

    public double getWidth() {
        return bounds.width;
    }

    public double getHeight() {
        return bounds.height;
    }
}

