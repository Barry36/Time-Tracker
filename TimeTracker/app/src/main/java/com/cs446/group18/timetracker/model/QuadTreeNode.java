package com.cs446.group18.timetracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuadTreeNode {
    public Rectangle2D bounds;
    public QuadTreeNode topLeftNode;
    public QuadTreeNode topRightNode;
    public QuadTreeNode bottomLeftNode;
    public QuadTreeNode bottomRightNode;
    public List<Neighbour> neighbours = new ArrayList<>();

    public QuadTreeNode(double latitude, double longitude, double latitudeRange, double longitudeRange) {
        bounds = new Rectangle2D(longitude, latitude, longitudeRange, latitudeRange);
    }

    public void addNeighbour(Neighbour neighbour, double deepestNodeSize) {
        double halfSize = bounds.width * .5f;
        if (halfSize < deepestNodeSize) {
            neighbours.add(neighbour);
            return;
        }

        QuadTreeNode node = locateAndCreateNodeForPoint(neighbour.getLatitude(), neighbour.getLongitude());
        node.addNeighbour(neighbour, deepestNodeSize);
    }

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

    public void findNeighboursWithinRectangle(Set<Neighbour> neighbourSet, Rectangle2D rangeAsRectangle) {
        boolean end;
        if (bounds.contains(rangeAsRectangle)) {
            end = true;
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

    private void addNeighbors(boolean contains, Set<Neighbour> neighborSet, Rectangle2D rangeAsRectangle) {
        if (contains) {
            neighborSet.addAll(neighbours);
            return;
        }

        findAll(neighborSet, rangeAsRectangle);
    }

    private void findAll(Set<Neighbour> neighborSet, Rectangle2D rangeAsRectangle) {
        for (Neighbour neighbor : neighbours) {
            if (rangeAsRectangle.contains(neighbor.getLongitude(), neighbor.getLatitude()))
                neighborSet.add(neighbor);
        }
    }

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

    public double getWidth() {
        return bounds.width;
    }

    public double getHeight() {
        return bounds.height;
    }
}

